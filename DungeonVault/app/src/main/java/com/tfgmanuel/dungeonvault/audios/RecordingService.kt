package com.tfgmanuel.dungeonvault.audios

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.repository.FileRepository
import com.tfgmanuel.dungeonvault.data.repository.TranscriptionRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

import android.util.Log

/**
 * Servicio en primer plano que graba audio en intervalos de 15 minutos.
 * Cada grabación se guarda en un archivo temporal, se sube a un servidor,
 * y luego puede eliminarse si es necesario.
 *
 * Este servicio funciona en segundo plano y usa Hilt para inyectar el repositorio
 * que maneja la subida de archivos.
 */
@AndroidEntryPoint
class RecordingService : Service () {

    @Volatile
    private var isPaused = false

    @Inject lateinit var fileRepository: FileRepository
    @Inject lateinit var transcriptionRepository: TranscriptionRepository

    private var mediaRecorder: MediaRecorder? = null
    private var currentFile: File? = null
    private var job: Job? = null
    private var campaignID: Int = -1
    private var name: String = ""


    /**
     * Método llamado cuando el sistema inicia el servicio.
     * Crea una notificación persistente (modo foreground) y lanza el bucle de grabación.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        campaignID = intent?.getIntExtra("campaign_id", -1) ?: -1
        if (campaignID == -1) {
            stopSelf()
            return START_NOT_STICKY
        }

        startForeground(1, createNotification())

        CoroutineScope(Dispatchers.IO).launch {
            val result = transcriptionRepository.createTranscription(campaignID)

            result.fold(
                onSuccess = { filename ->
                    name = filename
                    startRecordingLoop()
                },
                onFailure = { error ->
                    println("Error al iniciar transcripción: ${error.message}")
                    stopSelf()
                }
            )
        }

        return START_STICKY
    }

    /**
     * Inicia un bucle que graba audio durante 15 minutos,
     * luego detiene la grabación, sube el archivo, y repite.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecordingLoop() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                if (isPaused) {
                    delay(1000)
                    continue
                }

                currentFile = createAudioFile()
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(currentFile!!.absoluteFile)
                    prepare()
                    start()
                }

                delay(1 * 60 * 1000)

                mediaRecorder?.apply {
                    stop()
                    release()
                }

                val file = currentFile!!

                fileRepository.uploadFile(file)

                val result = transcriptionRepository.transcribe(
                    campaignID,
                    audio = file.name,
                    filename = name
                )

                result.onFailure {
                    println("Fallo en transcripción: ${it.message}")
                }

                file.delete()
            }

            mediaRecorder = null
        }
    }


    /**
     * Crea la notificación que mantiene vivo al servicio en primer plano.
     */
    private fun createNotification(): Notification {
        val channelId = "recording_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Grabación DnD", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Grabando partida de D&D")
            .setContentText("La grabación se está ejecutando en segundo plano")
            .setSmallIcon(R.drawable.ic_mic)
            .build()
    }

    /**
     * Crea un nuevo archivo temporal para guardar la grabación.
     * El nombre del archivo incluye un timestamp para evitar conflictos.
     */
    private fun createAudioFile(): File {
        val dir = File(getExternalFilesDir(null), "audio_temp")
        if (!dir.exists()) dir.mkdirs()
        return File(dir, "recording_${System.currentTimeMillis()}.mp4")
    }

    /**
     * Limpia los recursos cuando el servicio se destruye.
     */
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()

        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                release()
            }

            currentFile?.let { file ->
                CoroutineScope(Dispatchers.IO).launch {
                    fileRepository.uploadFile(file)
                    file.delete()
                }
            }
        }

        mediaRecorder = null

        if (campaignID != -1 && name.isNotBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = transcriptionRepository.cleanTranscription(campaignID, name)
                result.onSuccess {
                    println("Limpieza y resumen realizados correctamente")
                }.onFailure {
                    println("Error al realizar limpieza: ${it.message}")
                }
            }
        }
    }

    /**
     * Pausar la grabación
     */
    fun pauseRecording() {
        isPaused = true
    }

    /**
     * Iniciar la grabación.
     */
    fun resumeRecording() {
        isPaused = false
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
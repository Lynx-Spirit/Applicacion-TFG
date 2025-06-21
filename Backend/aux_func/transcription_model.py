import os
from pydub import AudioSegment
from config import Settings
import torch
from transformers import AutoModelForSpeechSeq2Seq, AutoProcessor, pipeline
from aux_func.whisper_singleton import whisper_instance
from aux_func.files_aux import delete
import asyncio
import aiofiles

def _convert_audio(audio: str, upload_folder = Settings.UPLOAD_FOLDER) -> str:
    """
    Transforma el audio del formato que tenía originalmente a .mp3 con un bitrate de 128k.
    Este archvio auxiliar usará el siguiente nombre → mombre_audio_aux.mp3

    Parámetros:
        audio: Nombre del fichero de audio del que se quiere transformar a .mp3.
        upload_folder: Donde se va a almacenar y obtener el fichero de audio.

    Retorna:
        str: Nombre del audio transformado.
    """
    new_audio = os.path.splitext(audio)[0] + ".mp3"
    old_path = os.path.join(upload_folder, audio)
    new_path = os.path.join(upload_folder, new_audio)

    sound = AudioSegment.from_file(old_path)
    sound.export(new_path, format="mp3", bitrate="128k")

    return new_audio

async def transcribe_audio(audio: str, file: str, upload_folder = Settings.UPLOAD_FOLDER):
    """
    Transcripción del audio pasado como parámetro y posterior escritura.
    Para la transcripción se usa whisper large-v3-turbo.

    Parámetros:
        audio: Nombre del fichero de audio original.
        file: Fichero de texto donde se va a almacenar el resultado de la tanscripción.
        upload_folder: Localización de los archivos.
    """
    aux = await asyncio.to_thread(_convert_audio, audio, upload_folder)

    audio_path = os.path.join(upload_folder, aux)
    result = await asyncio.to_thread(whisper_instance.transcribe, audio_path)

    file_path = os.path.join(upload_folder, file)
    async with aiofiles.open(file_path, "a", encoding="utf-8") as archivo:
        await archivo.write(" " + result["text"])
    
    await asyncio.to_thread(delete, aux)
    await asyncio.to_thread(delete, audio)

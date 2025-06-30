import os
from pydub import AudioSegment
from config import Settings
from sqlalchemy.orm import Session
from db.character_crud import get_characters_by_campaign
import torch
from transformers import AutoModelForSpeechSeq2Seq, AutoProcessor, pipeline
from aux_func.whisper_singleton import whisper_instance
from aux_func.files_aux import delete
import asyncio
import aiofiles
import re
from Levenshtein import distance

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

def text_cleanup(db: Session, id: int, text: str) -> str:
    """
    Realiza la limpieza del texto pasado como parámetro, la limpieza consiste en lo siguiente:
        1) Elimina palabras que estén repetidas (ej: si, si, si, si, si, ...)
        2) Elimina caracteres repetidos (ej: YYYYYYYYYYYY o  esteeeeeeeee)
        3) Corrige los nombres de los personajes (ej: Gol -> Gorl o Zephir -> Zephyr)
    
    Parámetros
        db: Sesión de la base de datos
        id: Identificador de la camapaña de la cual se obtendrán los personajes.
        text: Texto de entrada que se quiere limpiar.

    Retorna:
        Texto corregido.
    """
    characters = get_characters_by_campaign(db, id)
    names = [character.name for character in characters if character.visibility]

    #Quitar caracteres repetidos
    text = re.sub(r'(.)\1{5,}', r'\1', text)

    #Eliminar palabras repetidas que estén seguidas
    text = re.sub(r'(\b\w+\b)(?:[\s,]+(?:\1))+', r'\1', text)

    words = text.split()
    corrected_words = []

    for word in words:
        best_match = word
        min_distance = float('inf')

        for name in names:
            dist = distance(word.lower(), name.lower())
            if dist < min_distance and dist <= 2:
                min_distance = dist
                best_match = name

        corrected_words.append(best_match)

    text = ' '.join(corrected_words)
    
    return text

async def transcribe_audio(db: Session, id: int, audio: str, file: str, upload_folder = Settings.UPLOAD_FOLDER):
    """
    Transcripción del audio pasado como parámetro y posterior escritura.
    Para la transcripción se usa whisper large-v3-turbo.

    Parámetros:
        db: Sessión de la base de datos.
        id: Identificador de la camapaña de la cual se obtendrán los personajes.
        audio: Nombre del fichero de audio original.
        file: Fichero de texto donde se va a almacenar el resultado de la tanscripción.
        upload_folder: Localización de los archivos.
    """
    aux = await asyncio.to_thread(_convert_audio, audio, upload_folder)

    audio_path = os.path.join(upload_folder, aux)
    result = await asyncio.to_thread(whisper_instance.transcribe, audio_path)

    text = text_cleanup(db=db, id=id, text=result["text"])

    file_path = os.path.join(upload_folder, file)
    async with aiofiles.open(file_path, "a", encoding="utf-8") as archivo:
        await archivo.write(" " + text)
    
    await asyncio.to_thread(delete, aux)
    await asyncio.to_thread(delete, audio)

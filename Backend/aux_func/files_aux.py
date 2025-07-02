import os
from config import settings
from uuid import uuid4
import asyncio

async def save(file, upload_folder = settings.UPLOAD_FOLDER) -> str:
    """
    Función que almacena un archivo seleccionado dentro del servidor.

    Parámetros:
        file (UploadFile): Archivo recibido por parte de la aplicación.
        upload_folder (str): Ruta donde se almacenará el archivo.
            Por defecto, se toma de `settings.UPLOAD_FOLDER`.

    Retorna:
        str: Nombre del archivo guardado, junto a su extención original.
    """
    # Crear el directorio de carga si no existe
    os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)
    
    # Generar un nombre de archivo único y mantener su extensión original
    filename = f"{uuid4()}{os.path.splitext(file.filename)[1]}"  
    image_path = os.path.join(upload_folder, filename)

    # Guarda la imagen en el sistema de archivo.
    with open(image_path, "wb") as image_file:
        image_file.write(await file.read())

    return filename

def createFile(upload_folder= settings.UPLOAD_FOLDER) -> str:
    """
    Creación de un nuevo fichero de texto vacío.

    Parámetros:
        upload_folder (str): Ruta donde se almacenará el archivo.
            Por defecto, se toma de `settings.UPLOAD_FOLDER`.s

    Retorna:
        Nombre del nuevo fichero de texto
    """
    filename = f"{uuid4()}.txt"
    path = os.path.join(upload_folder, filename)

    with open(path, "w", encoding="utf-8") as f:
        f.write("")

    return filename

def delete(filename: str, upload_folder = settings.UPLOAD_FOLDER):
    """
    Elimina un archivo (por ejemplo, una imagen) del servidor si existe.

    Parámetros:
        filename (str): Nombre del archivo que se desea eliminar.
        upload_folder (str): Ruta del directorio donde se almacenan los archivos.
            Por defecto, se usa `settings.UPLOAD_FOLDER`.
    
    Retorna:
        None
    """
    if(filename.strip() != ""):
        path = os.path.join(upload_folder,filename)
        
        if os.path.exists(path):
            os.remove(path)

async def update(file_path: str, file):
    """
    Actualiza el fichero concreto.

    Parámetros:
        file (UploadFile): Archivo recibido por parte de la aplicación.
        file_path (str): Ruta donde se almacena el archivo.
    """
    with open(file_path,"wb") as f:
        content = await file.read()
        f.write(content)

async def cleanup_temp_files(files: list[str], upload_folder: str):
    """
    Elimina todos los archivos temporales pasados como parámetro.
    """
    for file in files:
        await asyncio.to_thread(delete, file)
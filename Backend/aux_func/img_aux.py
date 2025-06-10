import os
from config import settings
from uuid import uuid4

async def save(file, upload_folder = settings.UPLOAD_FOLDER) -> str:
    """
    Función que almacena la imagen seleccionada dentro del servidor.

    Parámetros:
        file (UploadFile): Imagen recibida por parte de la aplicación.
        upload_folder (str): Ruta donde se almacenará el archivo.
            Por defecto, se toma de `settings.UPLOAD_FOLDER`.

    Retorna:
        str: Nombre del archivo guardado, junto a su extención original.
    """
    # Crear el directorio de carga si no existe
    os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)
    
    # Generar un nombre de archivo único y mantener su extensión original
    image_filename = f"{uuid4()}{os.path.splitext(file.filename)[1]}"  
    image_path = os.path.join(upload_folder, image_filename)

    # Guarda la imagen en el sistema de archivo.
    with open(image_path, "wb") as image_file:
        image_file.write(await file.read())

    return image_filename

def delete(image_filename: str, upload_folder = settings.UPLOAD_FOLDER):
    """
    Elimina un archivo (por ejemplo, una imagen) del servidor si existe.

    Parámetros:
        image_filename (str): Nombre del archivo que se desea eliminar.
        upload_folder (str): Ruta del directorio donde se almacenan los archivos.
            Por defecto, se usa `settings.UPLOAD_FOLDER`.
    
    Retorna:
        None
    """
    if(image_filename.strip() != ""):
        path = os.path.join(upload_folder,image_filename)
        
        if os.path.exists(path):
            os.remove(path)
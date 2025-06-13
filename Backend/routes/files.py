import os
from fastapi import APIRouter, File, UploadFile, HTTPException
from fastapi.responses import FileResponse
from config import settings
from aux_func.files_aux import save

# Inicializa un enrutador para agrupar las rutas relacionadas con los ficheros.
router = APIRouter()

@router.post("/upload")
async def upload(file: UploadFile = File(...)):
    """
    Enpoint para la subida de los archivos.

    Parámetros:
        file (UploadFile): Imagen que quiere almacenarse.
    
    Retorna:
        dict: En caso de haberse subido correctamente, devuelve el nombre del archivo
            {"filename": <nombre del fichero.extensión original>}
    
    Lanza:
        HTTPException: En caso de haber un error durannte la subida se manda el mensaje "Error al subir el archivo"
    """
    try:
        file_name = await save(file)
        return {"filename": file_name}
    
    except Exception as e:
        raise HTTPException(status_code= 500, detail= "Error al subir el archivo")
    
@router.get("/{file_name}")
async def obtener_imagen(file_name: str):
    """
    Endpoint para para obtener un archivo concreto.

    Parámetros:
        file_name (str): Nombre del archivo de imagen que se quiere obtener.

    Retorna:
        FileResponse: El archivo de imagen solicitado si existe.

    Lanza:
        HTTPException: Se lanza en caso de no encontrarse el fichero en el servidor.
    """
    route = os.path.join(settings.UPLOAD_FOLDER, file_name)
    if os.path.exists(route):
        return FileResponse(route)
    else:
        raise HTTPException(status_code = 404, detail= "Fichero no encontrado")
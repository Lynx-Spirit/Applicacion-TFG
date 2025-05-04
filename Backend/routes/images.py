import os
from fastapi import APIRouter, File, UploadFile, HTTPException
from fastapi.responses import FileResponse
from config import settings
from aux_func.img_aux import save

router = APIRouter()

@router.post("/upload")
async def upload(file: UploadFile = File(...)):
    try:
        file_name = await save(file)
        return {"filename": file_name}
    
    except Exception as e:
        raise HTTPException(status_code= 500, detail= "Error al subir el archivo")
    
@router.get("/{file_name}")
async def obtener_imagen(file_name: str):
    route = os.path.join(settings.UPLOAD_FOLDER, file_name)
    if os.path.exists(route):
        return FileResponse(route)
    else:
        raise HTTPException(status_code = 404, detail= "Fichero no encontrado")
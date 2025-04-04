from fastapi import APIRouter, File, UploadFile, HTTPException
from aux_func.img_aux import save

router = APIRouter()

@router.post("/upload")
def upload(file: UploadFile = File(...)):
    try:
        url = save(file)
        return {"url": url}
    
    except Exception as e:
        raise HTTPException(status_code= 500, detail= "Error al subir el archivo")
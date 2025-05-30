import os
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from config import settings
from db.database import engine
from db.models import Base
from routes.auth import router as auth_router
from routes.images import router as img_router
from routes.campaigns import router as campaign_router

# Inicializa la app
app = FastAPI()

# Asegura que existe el directório de imágenes, en caso de que no exista, se crea ese directorio.
os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)

# Monta los archvios estáticos (imágenes)
app.mount("/static/images", StaticFiles(directory=settings.UPLOAD_FOLDER), name="images")

# Creación de las tablas de la base de datos
Base.metadata.create_all(bind=engine)

# Routers
app.include_router(auth_router, prefix="/auth")
app.include_router(img_router, prefix="/images")
app.include_router(campaign_router, prefix="/campaigns")
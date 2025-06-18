import os
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from config import settings
from db.database import engine
from db.models import Base
from routes.auth import router as auth_router
from routes.files import router as files_routes
from routes.campaigns import router as campaign_router
from routes.notes import router as note_router
from routes.characters import router as character_router
from routes.transcription import router as transcription_router

# Inicializa la app
app = FastAPI()

# Asegura que existe el directório de ficheros, en caso de que no exista, se crea ese directorio.
os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)

# Monta los archvios estáticos (ficheros)
app.mount("/static/files", StaticFiles(directory=settings.UPLOAD_FOLDER), name="files")

# Creación de las tablas de la base de datos
Base.metadata.create_all(bind=engine)

# Routers
app.include_router(auth_router, prefix="/auth")
app.include_router(files_routes, prefix="/files")
app.include_router(campaign_router, prefix="/campaigns")
app.include_router(note_router, prefix="/notes")
app.include_router(character_router, prefix="/characters")
app.include_router(transcription_router, prefix="/transcription")
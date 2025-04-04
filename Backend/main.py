import os
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from config import settings
from db.database import engine
from db.models import Base
from routes.auth import router as auth_router
from routes.images import router as img_router
from routes.campaigns import router as campaign_router

app = FastAPI()

os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)

app.mount("/images", StaticFiles(directory=settings.UPLOAD_FOLDER), name="images")

Base.metadata.create_all(bind=engine)

app.include_router(auth_router, prefix="/auth")
app.include_router(img_router, prefix="/images")
app.include_router(campaign_router, prefix="/campaigns")
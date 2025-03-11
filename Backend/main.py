from fastapi import FastAPI
from routes.auth import router as auth_router
from db.database import engine
from db.models import Base

app = FastAPI()

Base.metadata.create_all(bind=engine)

app.include_router(auth_router, prefix="/auth")
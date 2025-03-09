from fastapi import FastAPI
from routes.auth import router as auth_router
from db.database import engine
from db.models import Base

app = FastAPI()

Base.metadata.create_all(bind=engine)

app.add_api_route(auth_router,prefix="/auth")
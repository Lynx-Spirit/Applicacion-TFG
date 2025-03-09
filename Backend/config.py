from dotenv import load_dotenv
from pydantic_settings import BaseSettings
import os

load_dotenv()

class Settings(BaseSettings):
    DATABASE_URL: str = os.getenv("DATABASE_URL")
    SECRET_KEY: str = os.getenv("SECRET_KEY")
    ACCESS_TOKEN_EXPIRE_HOURS: int = int(os.getenv("ACCESS_TOKEN_EXPIRE_HOURS",12))
    REFRESH_TOKEN_EXPIRE_DAYS: int = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS", 7))


    @property
    def access_token_expire_seconds(self):
        return self.ACCESS_TOKEN_EXPIRE_HOURS * 3600

    @property
    def access_token_expire_minutes(self):
        return self.ACCESS_TOKEN_EXPIRE_HOURS * 60

settings = Settings()
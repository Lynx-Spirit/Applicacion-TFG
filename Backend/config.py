from dotenv import load_dotenv
from typing import ClassVar
from pydantic_settings import BaseSettings
import os

# Carga de los datos del .env a la aplicación
load_dotenv(dotenv_path="./.env")

class Settings(BaseSettings):
    """
    Configuración principal de la aplicación, carga las variables de entorno.

    Attributes:
        DATABASE_URL (str): URL de conexión de la base de datos.
        SECRET_KEY (str): clave secreta para la firma de los Tokens.
        ACCESS_TOKEN_EXPIRE_HOURS (int): Tiempo de expiración en horas del token de aceso.
        REFRESH_TOKEN_EXPIRE_DAYS (int): Tiempo de expiración en días del token de refresco.
        UPLOAD_FOLDER (ClassVar[str]): Carpeta donde se almacenan las imágenes subidas.
    """
    DATABASE_URL: str = os.getenv("DATABASE_URL")
    SECRET_KEY: str = os.getenv("SECRET_KEY")
    ACCESS_TOKEN_EXPIRE_HOURS: int = int(os.getenv("ACCESS_TOKEN_EXPIRE_HOURS",12))
    REFRESH_TOKEN_EXPIRE_DAYS: int = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS", 7))
    UPLOAD_FOLDER: ClassVar[str] = "imagenes"


    @property
    def access_token_expire_seconds(self):
        return self.ACCESS_TOKEN_EXPIRE_HOURS * 3600

    @property
    def access_token_expire_minutes(self):
        return self.ACCESS_TOKEN_EXPIRE_HOURS * 60

# Instancia de configuración para usar en toda la aplicación.
settings = Settings()
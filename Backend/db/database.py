from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.exc import SQLAlchemyError
from config import settings

DATABASE_URL = settings.DATABASE_URL

# Motor de SQLAlchemy para gestionar la conexión con la base de datos.
engine = create_engine(DATABASE_URL)

# Configuración de la sesión para la base de datos.
# No se actualiza de forma automática los cambios ni el estado del objeto.
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

def get_db():
    """
    Generador que proporciona una sesión de base de datos para usar en operaciones ORM.
    
    Yields:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    """
    db = SessionLocal()
    try:
        yield db
    except SQLAlchemyError as e:
        print(f"Error de conexión a la base de datos: {e}")
        db.rollback()
        raise
    finally:
        db.close()
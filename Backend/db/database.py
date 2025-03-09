from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.exc import SQLAlchemyError
from config import Settings

URL_DATABASE = Settings.DATABASE_URL

engine = create_engine(URL_DATABASE)

SessionLocal = sessionmaker(autocommit= False, autoflush= False, bind= engine)

Base = declarative_base()

#Función para obtener la sesión e la base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    except SQLAlchemyError as e:
        print(f"Error de conexión a la base de datos: {e}")
        db.rollback()
    finally:
        db.close()
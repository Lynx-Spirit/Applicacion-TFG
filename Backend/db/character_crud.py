from sqlalchemy.orm import Session
from typing import List
from aux_func.files_aux import delete
from db.models import Character
import datetime

#CRUD de los personajes

def create_character(db: Session, campaign_id: int, user_id: int, name: str, description: str, filename_backstory: str, img_name: str, visibility: bool) -> Character:
    """
    Crea un nuevo personaje asociado a un usuario y una campaña.

    Paámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña.
        user_id (int): Identificador del usuario que ha creado el personaje.
        name (str): Nombre del personaje.
        description (str): Descripciónd el personaje.
        filename_backstory (str): Nombre del fichero que contiene el backstory.
        visibility (bool): Visibilidad de la nota.

    Retorna:
        Character: Personaje creado con toda la información.
    """
    character = Character(
        campaign_id=campaign_id,
        user_id=user_id,
        name=name,
        description=description,
        filename_backstory=filename_backstory,
        img_name=img_name,
        visibility=visibility
    )

    db.add(character)
    db.commit()
    db.refresh(character)

    return character

def get_character_by_id(db: Session, character_id: int) -> Character:
    """
    Búsqueda de un personaje por su identificador.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        character_id (int): Identificador del personaje a buscar.

    Retorna:
        Character: Personaje que tenga el identificador pasado como parámetro.
    """
    return db.query(Character).filter(Character.id == character_id).first()

def get_characters_by_campaign(db: Session, campaign_id: int) -> List[Character]:
    """
    Obtención de todos los personajes de una campaña.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña.

    Retorna:
        List[Character]: Personajes que estén en la campaña pasada como parámetro.
    """
    return db.query(Character).filter(Character.campaign_id == campaign_id).all()

def update_character(db: Session, character_id: int, name: str, description: str, filename_backstory: str, img_name: str, visibility: bool) -> Character:
    """
    Actualiza la información del personaje de un usuario.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        character_id (int): Identificador del personaje del que se quiere actualizar la información.
        name (str): Nuevo nombre de personaje
        description (str): Nueva descripción del perosnaje.
        filename_backstory (str): Nombre del fichero de texto del backstory.
        img_name (str): Nombre del fichero de imagen del personaje.
        visibility (bool): Visibilidad actualizada.

    Retorna:
        Character: Personaje con toda la información actualizada.
    """
    character = get_character_by_id(db=db, character_id=character_id)

    if(name != "" and character.name != name):
        character.name = name

    if(description != "" and character.description != description):
        character.description = description
    
    if(filename_backstory != "" and character.filename_backstory != filename_backstory):
        delete(character.filename_backstory)
        character.filename_backstory = filename_backstory
    
    if(img_name != "" and character.img_name != img_name):
        delete(character.img_name)
        character.img_name = img_name

    if(character.visibility != visibility):
        character.visibility = visibility

    db.commit()
    db.refresh(character)

    return character

def remove_character(db: Session, character_id: int):
    """
    Elimina al personaje seleccionado.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        character_id (int): Identificador del personaje que se quiere eliminar.

    Retorna:
        None
    """
    character = get_character_by_id(db=db, character_id=character_id)

    if character:
        delete(character.img_name)
        delete(character.filename_backstory)
        db.delete(character)
        db.commit()
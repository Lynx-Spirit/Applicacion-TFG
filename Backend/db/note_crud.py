from sqlalchemy.orm import Session
from typing import List
from aux_func.files_aux import delete
from db.models import Note
from datetime import date

# CRUD de las notas

def create_note(db: Session, campaign_id: int, user_id: int, title: str, file_name: str, visibility: bool) -> Note:
    """
    Crea una nueva nota en la base de datos.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña en la que se encuentra la nota.
        user_id (int): Identificador del usuario que ha creado la campaña.
        title (str): Título de la nota.
        file_name (str): Nombre del fichero de la nota.
        visibility (bool): Visibilidad de la nota.

    Retorna:
        Note: Nota creada con toda la informacíon.
    """
    note = Note(
        campaign_id=campaign_id,
        user_id=user_id,
        creation_date=date.today(),
        title=title,
        file_name=file_name,
        visibility=visibility
    )

    db.add(note)
    db.commmit()
    db.refresh(note)

    return note

def get_note_by_id(db: Session, note_id: int) -> Note:
    """
    Obtención de nota por su identificador.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        note_id (int): Identificador de la nota a buscar.

    Retorna:
        Note: Nota que tenga el identificador pasado como parámetro.
    """
    return db.query(Note).filter(Note.id == note_id).first()

def get_campaigns_notes(db: Session, campaign_id: int) -> List[Note]:
    """
    Obtención de las notas que pertenezcan a la campaña.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña de la que se quiere obtener las notas.

    Retorna:
        List[Note]: Notas que estén en la campaña pasada como parámetro.
    """
    return db.query(Note).filter(Note.campaign_id == campaign_id).all()

def update_note(db: Session, note_id: int, title: str, file_name: str, visibility: bool) -> Note:
    note = get_note_by_id(db=db, note_id=note_id)

    if(title != "" and title != note.title):
        note.title = title

    if(file_name != "" and file_name != note.file_name):
        delete(note.file_name)
        note.file_name = file_name

    if(visibility != note.visibility):
        note.visibility = visibility

    db.commit()
    db.refresh()

    return note

def remove_note(db: Session, note_id: int):
    note = get_note_by_id(db=db, note_id=note_id)

    if(note):
        delete(note.file_name)
        db.delete(note)
        db.commit()
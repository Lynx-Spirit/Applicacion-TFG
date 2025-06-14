from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.note_crud import *
from db.campaign_crud import get_campaign_by_id
from aux_func.auth import get_current_user
from schema import note_response, note, note_update

# Inicializa el enrutador para agrupar las rutas relacionadas con las notas y transcripciones.
router = APIRouter()

@router.post("/new", response_model=note_response)
def create(note: note, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite crear a un usuario autenticado una nota en una campaña.

    Parámetros:
        note (note): Objecto con los datos necesarios para crear una nueva nota.
            Incluye: título, nombre del fichero y visibilidad de la nota.
        campaign_id (int): Camapaña en la que se va a insertar la nota.
        user_id (int): Es inyectado automáticamentep por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        note_reponse: Objeto con toda la información de la nota recién creada.

    Lanza:
        HTTPException: En caso de que el usuario inserte una nota en una campaña en la que no está como miembro, lanza un error.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=note.campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise  HTTPException(status_code=403, detail="Not autorized to insert a new note on this campaign")
    
    note = create_note(
        db=db,
        campaign_id=note.campaign_id,
        user_id=user_id,
        title=note.title,
        file_name=note.file_name,
        visibility=note.visibility
    )

    return note

@router.get("/{id}", response_model=note_response)
def get_note(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para obtener información concreta de una nota.

    Parámetros:
        id (int): Identificador de la nota que se desea consultar.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        note_response: Objeto con toda la información relativa a la nota.

    Lanza:
        HTTPException: Se lanza en caso de que un usuario que pertenezca a la campaña intenta acceder a la nota si no está visible (excepto el DM de esa partida y el creador)
    """
    note = get_note_by_id(db=db, note_id=id)
    campaign = get_campaign_by_id(db=db, campaign_id=note.campaign_id)

    user_not_in_campaign = int(user_id) not in [int(member.id) for member in campaign.members]

    if user_not_in_campaign and (not note.visibility or note.user_id != int(user_id) or campaign.creator_id != int(user_id)):
        raise HTTPException(status_code=403, detail="You are not allowed to see this note")
    
    return note

@router.get("/", response_model=List[note_response])
def get_notes(campaign_id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para obtener todas la notas de a campaña.

    Parámetros:
        campaign_id (int): Identificación de la campaña de la que se queire obtener todas las notas
        user_id (int): Inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtieene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para poder acceder a la base de datos.

    Retorna:
        List[note_response]: Lista de todas las notas que tiene la campaña.

    Lanza:
        HTTPException: En caso de que el usuario no forme parte de la campaña de la que quiere obtener las notas.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise HTTPException(status_code=403, detail="You are not allowed to see the campaign notes")
    
    return get_campaigns_notes(db=db, campaign_id=campaign_id)

@router.put("/{id}/update", response_model=note_response)
def update(id: int, note_info: note_update, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para actualizar los datos de una nota específica.

    Parámetros:
        id (int): Identificador de la nota que se quiere actualizar.
        note (note_update): Objeto que contiene toda la información actualizada de la nota.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        note_response: Nota con todos los datos actualizados.

    Lanza:
        HTTPException: Se lanza en caso de que un usuario distinto al creador, intente actualizar los datos de la nota.
    """
    note = get_note_by_id(db=db, note_id=id)

    if note.user_id != int(user_id):
        raise HTTPException(status_code=403, details="You are not allowed to update the information of this note")
    
    note = update_note(db=db,
                       note_id=id,
                       title=note_info.title, 
                       file_name=note_info.file_name, 
                       visibility=note_info.visibility
                       )
    
    return note

@router.delete("/{id}/delete")
def delete_note(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para eliminar una nota determinada.

    Parámetros:
        id (int): Identificador de la nota.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        {"message": "Nota eliminada correctamente"}
    
    Lanza:
        HTTPException: Si el usuario que quiere eliminar la nota no es el creador de esta, lanza un error.
    """

    note = get_note_by_id(db=db, note_id=id)

    if note.user_id != int(user_id):
        raise HTTPException(status_code=403, detail="You are not allowed to delete this note")
    
    remove_note(db=db, note_id=id)

    return {"message": "Nota eliminada correctamente"}
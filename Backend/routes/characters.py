from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.character_crud import *
from db.campaign_crud import get_campaign_by_id
from aux_func.auth import get_current_user
from schema import character, character_update, character_response

# Inicializa el enrutador para agrupar las rutas relacionadas con los personajes
router = APIRouter()

@router.post("/new", response_model=character_response)
def create(character: character, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite crear a un usuario autenticado un nuevo personaje en una campaña.

    Parámetros:
        character (character): Objecto con los datos necesarios para crear un nuevo personaje.
        campaign_id (int): Camapaña en la que se va a insertar el personaje.
        user_id (int): Es inyectado automáticamentep por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        note_reponse: Objeto con toda la información del personaje recién creado.

    Lanza:
        HTTPException: En caso de que el usuario inserte un personaje en una campaña en la que no está como miembro, lanza un error.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=character.campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise  HTTPException(status_code=403, detail="Not autorized to insert a new note on this campaign")
    
    character = create_character(
        db=db,
        campaign_id=character.campaign_id,
        user_id=character.user_id,
        name=character.name,
        filename_backstory=character.filename_backstory,
        img_name=character.img_name,
        visibility=character.visibility
    )

    return character

@router.get("/{id}", response_model=character_response)
def get_character(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para obtener informacíon concreta de un personaje.

     Parámetros:
        id (int): Identificador del personaje que se desea consultar.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

        Retorna:
        campaign_response: Objeto con toda la información relativa a la nota.

        Lanza:
            HTTPException: Se lanza en caso de que un usuario que pertenezca a la campaña intenta acceder al personaje si no está visible (excepto el DM de esa partida y el creador)
    """
    character = get_character_by_id(db=db, character_id=id)
    campaign = get_campaign_by_id(db=db, campaign_id=character.campaign_id)

    user_not_in_campaign = int(user_id) not in [int(member.id) for member in campaign.members]

    if user_not_in_campaign and (not character.visibility or character.user_id != int(user_id) or campaign.creator_id != int(user_id)):
        raise HTTPException(status_code=403, detail="You are not allowed to see this character")
    
    return character

@router.get("/", response_model=List[character_response])
def get_characters(campaign_id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para obtener todaos personajes de a campaña.

    Parámetros:
        campaign_id (int): Identificación de la campaña de la que se quiere obtener todos los personajes
        user_id (int): Inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para poder acceder a la base de datos.

    Retorna:
        List[note_response]: Lista de todos personajes que tiene la campaña.

    Lanza:
        HTTPException: En caso de que el usuario no forme parte de la campaña de la que quiere obtener los personajes.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise HTTPException(status_code=403, detail="You are not allowed to see the campaigns characters")
    
    return get_characters_by_campaign(db=db, campaign_id=campaign_id)

@router.put("/{id}/update", response_model=character_response)
def update(id: int, character_info: character_update, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para actualizar los datos de un personaje específico.

    Parámetros:
        id (int): Identificador de la nota que se quiere actualizar.
        character_info (character_update): Objeto que contiene toda la información actualizada del personaje.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        character_reponse: Personaje con todos los datos actualizados.

    Lanza:
        HTTPException: Se lanza en caso de que un usuario distinto al creador, intente actualizar los datos del personaje.
    """
    character = get_character_by_id(db=db, character_id=id)

    if character.user_id != int(user_id):
        raise HTTPException(status_code=403, details="You are not allowed to update the information of this character")
    
    character = update_character(db=db,
                            character_id=id,
                            name=character_info.name,
                            description=character_info.description,
                            filename_backstory=character_info.filename_backstory,
                            img_name=character_info.img_name,
                            visibility=character_info.visibility
                            )
    
    return character

@router.delete("/{id}/delete")
def delete_note(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para eliminar un personaje determinado.

    Parámetros:
        id (int): Identificador del personaje.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)' y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        None
    
    Lanza:
        HTTPException: Si el usuario que quiere eliminar un personaje del cual no es el creador de esta, lanza un error.
    """

    character = get_character_by_id(db=db, character_id=id)

    if character.user_id != int(user_id):
        raise HTTPException(status_code=403, detail="You are not allowed to delete this character")
    
    remove_character(db=db, character_id=id)
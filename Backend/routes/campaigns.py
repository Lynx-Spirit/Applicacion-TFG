from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.campaign_crud import *
from db.user_crud import get_users_campaigns
from schema import campaign, campaign_response, user_response, kick_info
from aux_func.campaigns_aux import generate_invite_code
from aux_func.auth import get_current_user

# Inicializa un enrutador para agrupar las rutas relacionadas con las campañas.
router = APIRouter()

@router.post("/new", response_model=campaign_response)
def create(campaign: campaign, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite a un usuario autenticado crear una nueva campaña.

    Parámetros:
        campaign (campaign): Objeto con los datos necesarios para crear una campaña
            Incluye: titulo, descripción y nombre de la imágen asociada a la campaña.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        campaign_response: Objeto con la información de la campaña recién creada, incluyendo el código de invitación.
    """
    invite_code = generate_invite_code(db)

    campaign = create_campaign(db, campaign.title, campaign.description, campaign.img_name, invite_code, user_id)

    return campaign

@router.get("/{id}", response_model=campaign_response)
def get_campaign(id: int, db: Session = Depends(get_db)):
    """
    Endpoint para obtener información concreta de una campaña.

    Parámetros:
        id (int): Identificador de la campaña que se desea consultar.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        campaign_response: Objeto con los detalles completos de la campaña.
            Incluye: su título, descripción, imagen, código de invitación y el ID del propietario.
    
    Lanza:
        HTTPException: En caso de que no haya una campaña con ese identificador.
    """
    campaign = get_campaign_by_id(db, id)

    if not campaign:
        raise HTTPException(status_code=404, detail="Campaign not found")
    
    return campaign

@router.get("/", response_model=list[campaign_response])
def get_campaigns(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para obtener todas las campañas de un usuario autenticado.

    Parámetros:
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    
    Retorna:
        List[campaign_response]: Listado de todas las camapañas en las que está.
    """
    campaigns = get_users_campaigns(db, user_id)

    return campaigns

@router.get("/{id}/members", response_model=list[user_response])
def get_campaign_members(id: int, user_id = Depends(get_current_user), db:Session = Depends(get_db)):
    """
    Enpoint que permite a un usuario siempre y cuando esté metido en la campaña, poder ver los usuarios que tiene esta

    Parámetros:
        id (int): Identificador de la campaña.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        List[user_response]: Listado de todos los usaurios de la campaña.

    Lanza:
        HTTPException: Se lanza en caso de que el usuario no forme parte de la campaña.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise HTTPException(status_code=403, detail="Not authorized to view members of this campaign")
    
    return campaign.members

@router.put("/{id}/update", response_model= campaign_response)
def update(id: int, campaign: campaign, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite a un usuarario siempre y cuando sea el creador de la campaña actualizar los datos de una campaña.

    Parámetros:
        id (int): Identificador de la camapaña.
        campaign (campaign): Objeto con los nuevos datos la campaña.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        campaign_response: En caso de que se actualice de forma correcta la campaña, se devolverá la campaña con los campos actualizados.

    Lanza:
        HTTPException: Se lanza en caso de un usuario distinto al creador de la campaña intente actualizar los datos de la campaña.
    """
    campaign_creator = get_campaign_creator(db,id)

    if int(user_id) == int(campaign_creator):
        return update_campaign(db, id, campaign.title, campaign.description, campaign.img_name)
    else:
        raise HTTPException(status_code=403, detail= "You are not the owner of this campaign")

@router.patch("/new-user", response_model=campaign_response)
def new_user(invite_code: str, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite a un usuario unirse a una partida nueva como jugador, usando un código de invitación.

    Parámetros:
        invite_code (str): Código de invitación de la campaña a la que el usuario quiere meterse.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        campaign_response: Objeto con la información de la campaña a la que el usuario se ha unido.
    
    Lanza:
        HTTPException 404: Si no se encuentra una campaña con el código proporcionado como parámetro.
        HTTPException 400: Si ya el usuario forma parte de la campaña, tanto como jugdor o porque es el propio Dungeon Master.
    """
    campaign = get_campaign_by_code(db, invite_code)
    
    if campaign is None:
        raise HTTPException(status_code=404, detail= "Campaign not found")
    
    if campaign.creator_id == user_id:
        raise HTTPException(status_code=400, detail="Creator cannot join the campaign")
    
    result = insert_user(db, campaign.id, user_id)

    if(result):
        return campaign
    else:
        raise HTTPException(status_code=400, detail="User already in campaign")

@router.patch("/{id}/remove-user")
def remove(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite eliminar una campaña específica de la cuenta del usuario. En caso de ser el Dungeon Master, se eliminará toda la partida

    Parámetros:
        id (int): Código de la campaña que se quiere eliminar.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        dict: En caso de eliminarse correctamente, se lanzará un mensaje que, en función de los permisos serán lo sigiuentes.
            -  Creador (Dungeon Master): {"message": "Camapaña eliminada correctamente"}
            -  Jugador:  {"message": "Usuario eliminado correctamente"}
    """
    campaign_creator = get_campaign_creator(db,id)

    if int(user_id) == int(campaign_creator):
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        remove_user(db, id, user_id)
        return {"message": "Usuario eliminado correctamente"}

@router.patch("/{id}/kick-user")
def kick(kick_info: kick_info, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite echar a un usuario seleccionado de una campaña específica.

    Parámetros:
        kick_info (kick_info): Objeto que contiene toda la información necesaria para poder echar al usuario de la partida.
            Contiene: email del usuario y el id de la campaña.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos

    Retorna:
        dict: En caso de eliminarse correctamente, se lanzará el siguiente mensaje:
            {"message": "Usuario eliminado correctamente"}
    
    Lanza:
        HTTPException: Se lanza en el caso de no ser el creador de la campaña.
    """
    campaign = get_campaign(id=kick_info.id, db=db)
    
    if campaign.creator_id != int(user_id):
        raise HTTPException(status_code=403, detail="No eres el creador de esta camapaña")
    
    remove_user(db=db, campaign_id=kick_info.id, user_id=kick_info.user)

    return {"message": "Usuario eliminado correctamente"}

@router.delete("/{id}/delete")
def delete(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permite eliminar una campaña específica.

    Parámetros:
        id (int): Código de la campaña que se quiere eliminar.
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        dict: En caso de eliminarse correctamente, se lanzará el siguiente mensaje:
            {"message": "Camapaña eliminada correctamente"}
    
    Lanza:
        HTTPException: Se lanza en el caso de no ser el usuario de la campaña.
    """
    campaign_creator = get_campaign_creator(db,id)
    if int(user_id) == int(campaign_creator):
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        raise HTTPException(status_code=403, detail="No eres el creador de esta camapaña")
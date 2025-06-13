from sqlalchemy.orm import Session, selectinload
from aux_func.files_aux import delete
from db.models import Campaign, User, campaign_invites
from db.user_crud import get_user_by_id, get_user_by_email

# CRUD de campaña

def create_campaign(db: Session, title: str, description:str, img_name: str, invite_code:str, user_id: int) -> Campaign:
    """
    Crea una nueva campaña en la base de datos y asocia al usuario al mismo.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        title (str): Título de la campaña creada.
        description (str): Descripción de la campaña creada.
        img_name (str): Nombre del archivo de imagen asociada a la campaña.
        invite_code (str): Código de invitación de la campaña.
        user_id (int): Usuario creador de la campaña.

    Retorna:
        Campaign: Campaña creada con toda la información.
    """
    campaign = Campaign(
        title=title,
        description=description,
        img_name=img_name,
        invite_code=invite_code,
        creator_id=user_id
    )

    db.add(campaign)
    db.commit()
    db.refresh(campaign)

    insert_user(db=db, campaign_id=campaign.id, user_id=user_id)

    return campaign

def get_campaign_by_id(db: Session, campaign_id: int) -> Campaign:
    """
    Búsqueda de una campaña por su identificador.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña a buscar.

    Retorna:
        Campaign: Campaña que tenga el identificador pasado como parámetro.
    """
    return db.query(Campaign).options(selectinload(Campaign.members)).filter(Campaign.id == campaign_id).first()

def get_campaign_by_code(db: Session, campaign_code: str) -> Campaign:
    """
    Búsqueda de una campaña por su código de invitación.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_code (int): Código de invitación de la campaña a buscar.

    Retorna:
        Campaign: Campaña que tenga el código de invitación pasado como parámetro.
    """
    return db.query(Campaign).filter(Campaign.invite_code == campaign_code).first()

def get_campaign_creator(db: Session, campaign_id: int) -> int:
    """
    Obtiene el identificador del creador de una campaña específica.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña, de la que se quiere obtener el creador.

    Retorna:
        int: Identificador del usuario que creó la campaña.
    """
    campaign = db.query(Campaign).filter(Campaign.id == campaign_id).first()
    return campaign.creator_id

def user_in_campaign(db: Session, campaign_id: int, user_id: int) -> bool:
    """
    Verifica si un usuario es parte de una campaña específica.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña en la que se quiere buscar al usuario.
        user_id (int): Identificador del usuario que se desea verificar.

    Retorna:
        bool: True si el usuario pertenece a la campaña. False en caso contrario. 
    """
    exists = db.query(campaign_invites).filter_by(
        campaign_id = campaign_id,
        user_id = user_id
    ).first()

    return exists is not None
 

def update_campaign(db: Session, campaign_id: int,  title: str = "", description: str= "", img_name: str= "") -> Campaign:
    """
    Actualiza los datos de una campaña existente, en aquellos campos que sean distintos a los actuales.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la campaña que se desea actualizar.
        title (str): Nuevo título de la campaña. Si es una cadena vacía o es igual al título actual, no se actualiza.
        description (str): Nueva descripción de la campaña. Si es una cadena vacía o es igual a la descipción actual, no se actualiza.
        img_name (str): Nuevo nombre del archivo de la imagen asociada. Si es una cadena vacía o es igual al nombre del archivo actual, no se actualiza

    Retorna:
        Campaign: Campaña actualizada con los datos nuevos pasados como parámetro.
    """
    campaign = get_campaign_by_id(db= db, campaign_id=campaign_id)
    
    if title  != "" and title != campaign.title:
        campaign.title = title
    
    if description != "" and description != campaign.description:
        campaign.description = description
    
    if img_name != "" and img_name != campaign.img_name:
        delete(filename=campaign.img_name)
        campaign.img_name = img_name

    db.commit()
    db.refresh(campaign)

    return campaign

def delete_campaign(db: Session, campaign_id: int):

    """
    Elimina la camapaña seleccionada.
    
    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): Identificador de la camapaña a eliminar.
        
    Retorna:
        None
    """
    campaign = get_campaign_by_id(db= db, campaign_id=campaign_id)

    delete(campaign.img_name)
    db.delete(campaign)
    db.commit()

def insert_user(db: Session, campaign_id: int, user_id: int) -> bool:
    """
    Inserta un usuario específico a una campaña concreta, si aún no está agregado.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): ID de la campaña.
        user_id (int): ID del usuario a insertar.

    Retorna:
        bool: True si se agregó exitosamente. False si ya existía o hubo un problema.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)
    user = get_user_by_id(db=db, user_id=user_id)

    if not user_in_campaign(db=db, campaign_id=campaign_id, user_id=user_id):
        campaign.members.append(user)
        db.commit()
        db.refresh(user)
        return True
    return False

def remove_user(db: Session, campaign_id: int, user_id: int):
    """
    Elimina a un usuario específico de una campaña concreta.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        campaign_id (int): ID de la campaña.
        user_id (int): ID del usuario a eliminar.
    
    Retorna:
        None
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)
    user = get_user_by_id(db, user_id)

    campaign.members.remove(user)
    db.commit()
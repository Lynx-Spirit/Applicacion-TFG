from sqlalchemy.orm import Session
from aux_func.img_aux import delete
from db.models import Campaign, User, campaign_invites
from db.user_crud import get_user_by_id

#Crear campaña, eliminar campaña, editar título o descripción, insertar más gente a la camapaña, quitar a gente de la campaña.

def create_campaign(db: Session, title: str, description:str, img_name: str, invite_code:str, user_id: int):
    campaign = Campaign(
        title= title,
        description= description,
        img_name= img_name,
        invite_code = invite_code,
        creator_id= user_id
    )

    db.add(campaign)
    db.commit()
    db.refresh(campaign)

    insert_user(db, campaign.id, user_id)

    return campaign

def get_campaign_by_id(db: Session, campaign_id: int):
    return db.query(Campaign).filter(Campaign.id == campaign_id).first()

def get_campaign_by_code(db: Session, campaign_code: str):
    return db.query(Campaign).filter(Campaign.invite_code == campaign_code).first()

def get_campaign_creator(db: Session, campaign_code: str):
    campaign = db.query(Campaign).filter(Campaign.id == campaign_code).first()
    return campaign.creator_id

def user_in_campaign(db: Session, campaign_id: int, user_id: int) -> bool:
    exists = db.query(campaign_invites).filter_by(
        campaign_id = campaign_id,
        user_id = user_id
    ).first()

    return exists is not None
 

def update_campaign(db: Session, campaign_id: int,  title: str = None, description: str= None, img_name: str= None):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    
    if title and title != campaign.title:
        campaign.title = title
    
    if description and description != campaign.description:
        campaign.description = description
    
    if img_name and img_name != campaign.img_name:
        delete(campaign.img_name)
        campaign.img_name = img_name

    db.commit()
    db.refresh(campaign)

    return campaign

def delete_campaign(db: Session, id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= id)

    delete(campaign.img_name)
    db.delete(campaign)
    db.commit()

def insert_user(db: Session, campaign_id: int, user_id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    user = get_user_by_id(db, user_id)

    if not user_in_campaign(db, campaign_id = campaign_id, user_id = user_id):
        campaign.members.append(user)
        db.commit()
        db.refresh(user)
        return True
    return False

def remove_user(db: Session, campaign_id: int, user_id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    user = get_user_by_id(db, user_id)

    campaign.members.remove(user)
    db.commit()
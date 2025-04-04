from sqlalchemy.orm import Session
from aux_func.img_aux import delete, save
from db.models import Campaign, User
from db.user_crud import get_user_by_id

#Crear campaña, eliminar campaña, editar título o descripción, insertar más gente a la camapaña, quitar a gente de la campaña.

def create_campaign(db: Session, title: str, description:str, img_url: str, invite_code:str, user_id: int):
    campaign = Campaign(
        title= title,
        description= description,
        imgURL= img_url,
        invite_code = invite_code,
        creatorID= user_id
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
    campaign = get_campaign_by_id(db, campaign_id)
    user = get_user_by_id(db, user_id)

    return user in campaign.members
 

def update_campaign(db: Session, campaign_id: int,  title: str = "", desciption: str= "", img_url: str= ""):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    
    if title != "":
        campaign.title = title
    
    if desciption != "":
        campaign.description = desciption
    
    if img_url != "":
        delete(campaign.imgURL)
        campaign.imgURL = img_url

    db.commit()
    db.refresh(campaign)

    return campaign

def delete_campaign(db: Session, id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= id)

    delete(campaign.imgURL)
    db.delete(campaign)
    db.commit()

def insert_user(db: Session, campaign_id: int, user_id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    user = get_user_by_id(db, user_id)

    if user not in campaign.members:
        campaign.members.append(user)
        db.commit()

def remove_user(db: Session, campaign_id: int, user_id: int):
    campaign = get_campaign_by_id(db= db, campaign_id= campaign_id)
    user = get_user_by_id(db, user_id)

    campaign.members.remove(user)
    db.commit()
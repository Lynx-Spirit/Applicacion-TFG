from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.campaign_crud import *
from db.user_crud import get_users_campaigns
from schema import Campaign, CampaignResponse
from aux_func.general import get_user_id
from aux_func.campaigns_aux import generate_invite_code

router = APIRouter()

@router.post("/")
def create(campaign: Campaign, userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    invite_code = generate_invite_code(db)

    create_campaign(db, campaign.title, campaign.description, campaign.img_url, invite_code, user_id)

    return {"message": "Camapaña creada exitosamente"}

@router.get("/{id}", response_model= CampaignResponse)
def get_campaign(id: int, db: Session = Depends(get_db)):
    campaign = get_campaign_by_id(db, id)

    if not campaign:
        raise HTTPException(status_code=404, detail="Campaign not found")
    
    return campaign

@router.get("/", response_model=list[Campaign])
def get_campaigns(userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    campagins = get_users_campaigns(db, user_id)

    return campagins

@router.put("/{id}/update")
def update(id: int, campaign: Campaign, userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    campaign_creator = get_campaign_creator(db,id)

    if user_id == campaign_creator:
        update_campaign(db, id, campaign.title, campaign.description, campaign.img_url)
        return {"message": "Camapaña modificada de forma exitosa"}
    else:
        raise HTTPException(status_code=401, detail= "You are not the owner of this campaign")

@router.patch("/new-user")
def new_user(invite_code: str, userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    campaign = get_campaign_by_code(db, invite_code)
    
    if campaign is None:
        raise HTTPException(status_code=401, detail= "Campaign not found")
    
    insert_user(db, campaign.id, user_id)

    return {"message": "Usuario insertado de forma exitosa"}

@router.patch("/{id}/remove-user")
def remove(id: int, userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    campaign_creator = get_campaign_creator(db,id)

    if user_id == campaign_creator:
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        remove_user(db, id, user_id)
        return {"message": "Usuario eliminado correctamente"}

@router.delete("/{id}")
def eliminate(id: int, userToken: str, db: Session = Depends(get_db)):
    user_id = get_user_id(userToken)
    campaign_creator = get_campaign_creator(db,id)

    if user_id == campaign_creator:
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        raise HTTPException(status_code=401, detail="No eres el creador de esta camapaña")
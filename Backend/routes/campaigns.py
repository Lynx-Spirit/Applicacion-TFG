from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.campaign_crud import *
from db.user_crud import get_users_campaigns
from schema import Campaign, CampaignResponse
from aux_func.campaigns_aux import generate_invite_code
from aux_func.auth import get_current_user, get_user_id

router = APIRouter()

@router.post("/new", response_model= CampaignResponse)
def create(campaign: Campaign, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    invite_code = generate_invite_code(db)

    campaign = create_campaign(db, campaign.title, campaign.description, campaign.img_name, invite_code, user_id)

    return campaign

@router.get("/{id}", response_model= CampaignResponse)
def get_campaign(id: int, db: Session = Depends(get_db)):
    campaign = get_campaign_by_id(db, id)

    if not campaign:
        raise HTTPException(status_code=404, detail="Campaign not found")
    
    return campaign

@router.get("/", response_model=list[CampaignResponse])
def get_campaigns(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    campaigns = get_users_campaigns(db, user_id)

    return campaigns

@router.put("/{id}/update", response_model= CampaignResponse)
def update(id: int, campaign: Campaign, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    campaign_creator = get_campaign_creator(db,id)

    if int(user_id) == int(campaign_creator):
        return update_campaign(db, id, campaign.title, campaign.description, campaign.img_name)
    else:
        raise HTTPException(status_code=403, detail= "You are not the owner of this campaign")

@router.patch("/new-user", response_model= CampaignResponse)
def new_user(invite_code: str, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
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
    campaign_creator = get_campaign_creator(db,id)

    if int(user_id) == int(campaign_creator):
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        remove_user(db, id, user_id)
        return {"message": "Usuario eliminado correctamente"}

@router.delete("/{id}/delete")
def delete(id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    campaign_creator = get_campaign_creator(db,id)
    if int(user_id) == int(campaign_creator):
        delete_campaign(db, id)
        return {"message": "Camapaña eliminada correctamente"}
    else:
        raise HTTPException(status_code=401, detail="No eres el creador de esta camapaña")
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from db.database import get_db
from db.note_crud import *
from db.campaign_crud import get_campaign_by_id
from aux_func.auth import get_current_user
from aux_func.files_aux import createFile, delete
from schema import note_response, transcribe_info, clean_info
import datetime

#Inicializa el enrutador para agrupar las rutas relacionadas con las transcripciontes
router = APIRouter()

@router.post("/start", response_model=note_response)
def create(campaign_id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Enpoint que permite crear la transcripción a un usuario autenticado en una campaña determinada.router

    Parámetros:
        campaign_id (int): Camapaña en la que se va a insertar la nueva transcripción
        user_id (int): Es inyectado de forma automática por 'Depends(get_current_user)' y de ahí se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        mensaje de que se ha creado de forma correcta

    Lanza:
        HTTPException: En caso de que el usuario que lo vaya a insertar no forme parte de la campaña de rol.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise HTTPException(status_code=403, detail="You are not autorized to create a new transcription in this campaign.")
    
    hoy = datetime.datetime.now()

    filename = createFile()

    note = create_note(
        db=db,
        campaign_id=campaign_id,
        user_id=None,
        title=f"Transcripción sesión {hoy.day} de {hoy.strftime('%B')}",
        file_name=filename,
        visibility=True
    )

    return note

@router.put("/transcribe")
async def transcribe(information: transcribe_info, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permmite transcribir el fichero que se ha pasado como parámetro.

    Parámetros:
        information (transcribe_info): Objeto con toda la información necesaria para poder realizar la transcripción.
        user_id (int): Es inyectado de forma automática por 'Depends(get_current_user)' y de ahí se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        Mensaje de que se ha trancrito todo de forma correcta.
    """
    
    # await trasncribe_file(information) aquí se realizará la transcripción solamente
    # await delete(information.audio)
    return {"message": "Transcripción realizada correctamente"}

@router.put("/clean", response_model=note_response)
async def clean(information: clean_info, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para realizar la limpieza del archivo de la transcripción y realice el resumen.

    Parámetros:
        information (clean_info): Objeto con toda la información necesaria para poder realizar la limpieza y resumen.
        user_id (int): Es inyectado de forma automática por 'Depends(get_current_user)' y de ahí se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        Mensaje de que se realizado todo de forma correcta.
    """
    # await clean_transcription()
    # await create_summary()
    return None
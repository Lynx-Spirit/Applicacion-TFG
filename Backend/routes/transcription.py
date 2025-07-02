from fastapi import APIRouter, Depends, HTTPException, BackgroundTasks
from sqlalchemy.orm import Session
from db.database import get_db
from db.note_crud import *
from db.campaign_crud import get_campaign_by_id
from aux_func.auth import get_current_user
from aux_func.files_aux import createFile, delete
from schema import note_response, transcribe_info, clean_info, transcribe_init
from aux_func.transcription_model import transcribe_audio
import datetime
import asyncio

#Inicializa el enrutador para agrupar las rutas relacionadas con las transcripciontes
router = APIRouter()

@router.post("/start", response_model=transcribe_init)
def create(campaign_id: int, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Enpoint que permite crear la transcripción a un usuario autenticado en una campaña determinada.

    Parámetros:
        campaign_id (int): Camapaña en la que se va a insertar la nueva transcripción
        user_id (int): Es inyectado de forma automática por 'Depends(get_current_user)' y de ahí se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
        Nombre del fichero de la nota y del resumen.

    Lanza:
        HTTPException: En caso de que el usuario que lo vaya a insertar no forme parte de la campaña de rol.
    """
    campaign = get_campaign_by_id(db=db, campaign_id=campaign_id)

    if int(user_id) not in [int(member.id) for member in campaign.members]:
        raise HTTPException(status_code=403, detail="You are not autorized to create a new transcription in this campaign.")
    
    hoy = datetime.datetime.now()

    filename = createFile()
    summary_file = createFile()

    note = create_note(
        db=db,
        campaign_id=campaign_id,
        user_id=None,
        title=f"Transcripción sesión {hoy.day} de {hoy.strftime('%B')}",
        file_name=filename,
        visibility=True
    )

    summary = create_note(
        db=db,
        campaign_id=campaign_id,
        user_id=None,
        title=f"Resumen sesión {hoy.day} de {hoy.strftime('%B')}",
        file_name=summary_file,
        visibility=True
    )

    result = transcribe_init(filename=note.file_name, summary= summary.file_name)

    return result

@router.put("/transcribe")
async def transcribe(information: transcribe_info, background_tasks: BackgroundTasks, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint que permmite transcribir el fichero que se ha pasado como parámetro.

    Parámetros:
        information (transcribe_info): Objeto con toda la información necesaria para poder realizar la transcripción.
        user_id (int): Es inyectado de forma automática por 'Depends(get_current_user)' y de ahí se obtiene el identificador del usuario.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
    
    Retorna:
        Mensaje de que se ha trancrito todo de forma correcta.
    """
    background_tasks.add_task(
        transcribe_audio,
        db,
        information.campaign_id,
        information.audio,
        information.filename,
        information.summary
    )
    
    return {"message": "Transcripción realizada correctamente"}

@router.put("/clean") #, response_model=note_response
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
    return {"message": "Transcripción realizada correctamente"}
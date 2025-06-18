from datetime import date
from pydantic import BaseModel, EmailStr
from typing import Optional

class Config:
    """
    Configuración común para los modelos (por ejemplo, compatibilidad con los ORMs).
    """
    from_attributes = True

class user(BaseModel):
    """
    Modelo de entrada para registrar un nuevo usuario a la aplicación.

    Atributos:
        email (EmailStr): Correo electrónico del usuario.
        password (str): Contraseña del usuario.
        avatar (str): Nombre del archivo del avatar.
        nickname (str): Apodo del usuario dentro de la aplicación.
    """
    email: EmailStr
    password: str
    nickname: str
    avatar: str

class user_update(BaseModel):
     """
     Modelo de entrada para actualizar un usuario ya registrado en la aplicacíon.

     Atributos:
        nickname (str): Nuevo apodo del usuario.
        avatar (str): Nuevo nombre del archivo del avatar.
     """
     nickname: str
     avatar: str

class user_response(BaseModel):
    """
    Modelo de respuesta con los datos públicos del usuario de la aplicación.

    Atributos:
        id (int): Identificador del usuario.
        email (EmailStr): Correo elecrónico del usuario.
        avatar (str): Nombre del archivo del avatar actual del usuario.
        nickname (str): Apodo actual del usuario.
    """
    id: int
    email: EmailStr
    avatar: str
    nickname: str

class login_info(BaseModel):
    """
    Modelo de entrada con los datos de inicio de sesión del usuario.

    Atributos:
        email (EmailStr): Correo electrónico del usuario.
        password (str): Contraseña del usaurio.
    """
    email: EmailStr
    password: str

class change_password_schema(BaseModel):
    """
    Modelo de entrada para cambiar la contraseña del usuario.

    Atributos:

    """
    email: EmailStr
    old_password: str
    new_password: str
        
class refresh_token_request(BaseModel):
    """
    Modelo de entrada para solicitar nuevos tokens usando el token de refreso.

    Atributos:
        refresh_token (str): Token de refresco del usuario.
    """
    refresh_token: str

class token_response(BaseModel):
    """
     Modelo de respuesta que contienen los tokens JWT.

     Atributos:
        access_token (str): Nuevo token de acceso.
        refresh_token (str): Nuevo token de refresco.
     """
    
    access_token: str
    refresh_token: str

class token(BaseModel):
     """
     Modelo genérico para representar un token simple.

     Atributos:
        token (str): Token JWT u otro tipo.
     """
     token: str

class campaign(BaseModel):
     """
     Modelo de entrada para la creación o actualización de una campaña.

     Atributos:
        title (str): Título de la campaña.
        description (str): Descripción de la campaña.
        img_name (str): Nombre del archivo de la imagen de la campaña.
     """
     title: str
     description: str
     img_name: str

class campaign_response(BaseModel):
     """
     Modelo de respuesta de la campaña.

     Atributos:
        id (int): Identificador interno de la campaña.
        title (str): Título de la campaña.
        description (str): Descripción de la campaña.
        img_name (str): Nombre del archivo de la imagen de la campaña.
        invite_code (str): Código de invitación de la campaña.
        creator_id (int): Identificador interno del usuario que ha creado la campaña (el Dungeon Master).
     """
     id: int
     title: str
     description: str
     img_name: str
     invite_code: str
     creator_id: int

class kick_info(BaseModel):
    """
    Modelo de entrada para poder eliminar a un usuaio de la campaña.

    Atributos:
        user (int): Id del usuario a eliminar de la campaña.
        id (int): Identificador de la campaña.
    """
    user : int
    id: int

class note(BaseModel):
    """
    Modelo de entrada para la creación de las notas.

    Atributos:
        campaign_id (int): Identificador de la campaña
        title (str): Título de la nota.
        file_name (str): Nombre del fichero de la nota.
        visibility (bool): Visibilidad de la nueva nota
    """
    campaign_id: int
    title: str
    file_name: str
    visibility: bool

class note_update(BaseModel):
    """
    Modelo de entrada para actualizar las notas.

    Atributos:
        title (str): Título de la nota.
        file_name (str): Nombre del fichero de la nota.
        visibility (bool): Visibilidad de la nueva nota
    """
    title: str
    file_name: str
    visibility: bool

class note_response(BaseModel):
    """
    Modelo de respuesta para la creación o actualización de una nota o transcripción.

    Atributos:
        id (int): Identificador de la nota.
        campaign_id (int): Identificador de la partida a la que pertenece.
        user_id (int): Ideniticador del usuario que lo ha creado.
        creation_date (date): Fecha de creaión de la nota.
        title (str): Título de la nota.
        file_name (str): Nombre del archivo que contiene toda la info de la nota.
        visibility (bool): Indica si la nota es visible o no al resto de usuarios.
    """
    id: int
    campaign_id: int
    user_id: Optional[int] 
    creation_date: date
    title: str
    file_name: str
    visibility: bool
 

class character(BaseModel):
    """
    Modelo de entrada para la creación de las notas.

    Atributos:
        campaign_id (int): Identificador de la campaña.
        name (str): Nombre del personaje.
        description (str): Descripción general del personaje
        filename_backstory (str): Nombre del archivo del backstory.
        img_name (str): Nombre del archivo de imagen del personaje.
        visibility (bool): Indica si el personaje está visible al resto de usuarios o no.
    """
    campaign_id: int
    name: str
    description: str
    filename_backstory: str
    img_name: str
    visibility: bool

class character_update(BaseModel):
    """
    Modelo de entrada para la actualización de las notas.

    Atributos:
        name (str): Nombre del personaje.
        description (str): Descripción general del personaje
        filename_backstory (str): Nombre del archivo del backstory.
        img_name (str): Nombre del archivo de imagen del personaje.
        visibility (bool): Indica si el personaje está visible al resto de usuarios o no.
    """
    name: str
    description: str
    filename_backstory: str
    img_name: str
    visibility: bool

class character_response(BaseModel):
    """
    Modelo de respuesta para las notas

    Atributos:
        id (int): Identificador interno de las notas.
        campaign_id (int): Identificador de la campaña.
        user_id (int): Identificador del usuario que ha creado el personaje.
        name (str): Nombre del personaje.
        description (str): Descripción general del personaje
        filename_backstory (str): Nombre del archivo del backstory.
        img_name (str): Nombre del archivo de imagen del personaje.
        visibility (bool): Indica si el personaje está visible al resto de usuarios o no.
    """
    id: int
    campaign_id: int
    user_id: int
    name: str
    description: str
    filename_backstory: str
    img_name: str
    visibility: bool

class transcribe_info(BaseModel):
    """
    Modelo de entrada con toda la información necesaria para poder realizar las transcripciones de forma correcta.

    Atributos:
        campaign_id (int): Identificación de la campaña la cual se va a guardar los datos.
        audio (str): Nombre del fichero de audio del cual se van a obtener los datos.
        filename (str): Nombre del fichero donde se va a guardar el resultado de la transcripción
    """
    campaign_id: int
    audio: str
    filename: str

class clean_info(BaseModel):
    """
    Modelo de entrada con toda la información necesaria para poder realizar la limpieza de la trancripción de forma correcta.

    Atributos:
        campaign_id (int): Identificación de la campaña la cual se va a guardar los datos.
        filename (str): Nombre del fichero donde se va a guardar el resultado de la transcripción
    """
    campaign_id: int
    filename: str
from pydantic import BaseModel, EmailStr

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

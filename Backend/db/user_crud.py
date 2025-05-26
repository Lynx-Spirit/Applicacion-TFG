from sqlalchemy.orm import Session
from aux_func.img_aux import delete
from aux_func.auth import hash_password
from typing import List
from db.models import User
from db.models import Campaign
from schema import user

def create_user(db: Session, user: user) -> User:
    """
    Crea un nuevo usuario en la base de datos.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        user (user): Objeto que contiene la información para la creación del usuario.
            Siendo esta el email, password, nickname y el avatar.

    Retorna: 
        User: El usuario insertado en la base de datos.
    """
    hashed_pw = hash_password(user.password)

    new_user = User(email= user.email, nickname= user.nickname, hashedPass= hashed_pw, avatar= user.avatar)

    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return new_user

def get_user_by_id(db: Session, user_id: int) -> User:
    """
    Búsqueda de un usuario por su identificador.
    
    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        user_id (int): Identificador del usuario a buscar.
    
    Retorna:
        User: Usuario que tenga el identificador pasado como parámetro.
    """
    return db.query(User).filter(User.id == user_id).first()

def get_user_by_email(db: Session, email: str) -> User:
    """
    Búsqueda de un usuario por su email.
    
    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        email (int): Email del usuario a buscar.
    
    Retorna:
        User: Usuario que tenga el email pasado como parámetro.
    """
    return db.query(User).filter(User.email == email).first()

def get_users_campaigns(db: Session, user_id: int) -> List[Campaign]:
    """
    Obtiene todas las campañas en las que participa un usuario.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        user_id (int): Identificador del usuario el cual se quiere obtener sus campañass.

    Retorna:
        List[Campaign]: Lista de objetos `Campaign` en los que el usuario participa.
    """
    user = get_user_by_id(db, user_id)
    return user.joined_campaigns

def update_password(db: Session, user_email: str, new_password: str) -> User:
    """
    Actualización de la contraseña del usuario.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        user_email (str): Email del usuario que se quiere cambiar la contraseña.
        new_password (str): Nueva contraseña.
    
    Retorna:
        User: Usuario con la contraseña actualizada.
    """
    user = get_user_by_email(db= db, email= user_email)

    user.hashed_password = hash_password(new_password)

    db.commit()
    db.refresh(user)

    return user

def update_user(db: Session, user_id: int, user_nickname: str = "", user_new_avatar: str = "") -> User:
    """
    Actualiza de la inforamción pública del usuario en aquellos campos que sean distintos a los actuales.

    Parámetros:
        db (Session): Sesión e SQLAlchemy para acceder a la base de datos.
        user_id (int): Identificador del usuario a actualizar los datos.
        user_nickname (str): Nuevo apodo del usuario. Si es una cadena vacía o es igual a al nickname actual, no se actualiza
        user_new_avatar (str): Nuevo nombre del arcvhivo de la imagen asociada. Si es una cadena vacía o es igual al nombre del archivo actual, no se actualiza.

    Retorna:
        User: Usuario actualizado con los nuevos datos pasados como parámetro.
    """
    user = get_user_by_id(db, user_id)

    if user_nickname != "" and user.nickname != user_nickname:
        user.nickname = user_nickname

    if user_new_avatar != "" and user.avatar != user_new_avatar:
        delete(user.avatar)
        user.avatar = user_new_avatar

    db.commit()
    db.refresh(user)

    return user

def remove_user(db: Session, user_id: int):
    """
    Elimina el usuario seleccionado.

    Parámetros:
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
        user_id (int): Identificador del usuario que se quiere eliminar.
    
    Retorna:
        None
    """
    user = get_user_by_id(db, user_id)

    if user:
        delete(user.avatar_file_name)
        db.delete(user)
        db.commit()
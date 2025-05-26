from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from db.database import get_db
from db.user_crud import get_user_by_email, create_user, update_password, remove_user, get_user_by_id, update_user
from schema import user, change_password_schema, token_response, refresh_token_request, token, login_info, user_response, user_update
from aux_func.auth import get_current_user
from aux_func.auth import create_access_token, create_refresh_token, verify, verify_token

# Inicializa un ennrutador para agrupar la rutas relacionadas con la autenticación y usuarios.
router = APIRouter()

@router.post("/register")
def register(user_data: user, db: Session = Depends(get_db)):
    """
    Endpoint para registrar un nuevo usuario en la aplicación.

    Verifica si el correo electrónico ya está en uso y, si no es así,
    crea un nuevo registro en la base de datos.

    Parámetros:
        user_data (user): Objeto que contiene los datos necesarios para registrar al usuario.
            Incluye: email, contraseña, nickname y avatar.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
            Este parámetro es inyectado automáticamente por la dependencia 'get_db'.

    Retorna:
        dict: Mensaje de confirmación si el usuario se ha creado correctamente.
            {"message": "Usuario creado exitosamente"}

    Lanza:
        HTTPException: Si ya existe un usuario con el correo proporcionado.
    """
    existing_user = get_user_by_email(db, user_data.email)
    if existing_user:
        raise HTTPException(status_code=400, detail="El usuario ya existe")

    user = create_user(db, user= user_data)
    return {"message": "Usuario creado exitosamente"}

@router.post("/login")
def login(user_data: login_info, db: Session = Depends(get_db)):
    """
    Enpoint para el inicio de sesión de los usuarios de la aplicación.

    Parámetros:
        user_data (login_info): Objeto que contiene la información de inicio de sesión  del usuario
            Incluye: email y contaseña.
        db (Session): Sesión de SQLAlchemy para acceder a la base de datos.
            Este parámetro es inyectado automáticamente por la dependencia 'get_db'.
    
    Retorna:
        dict: Un diccionario con los token JWT y el identificador del usuario si las credenciales son correctas
            {
                "access_token": <token de acceso>,
                "refresh_token": <token de refresco>,
                "token_type": "bearer",
                "user_id": <id_del_usuario>
            }
    
    Lanza:
        HTTPException: Si el usuario no existe o las credenciales son incorrectas.
    """
    user = get_user_by_email(db, user_data.email)
    if not user or not verify(user_data.password, user.hashedPass):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Credenciales incorrectas")

    access_token = create_access_token({"sub": str(user.id)})
    refresh_token = create_refresh_token({"sub": str(user.id)})
    return {"access_token": access_token, "refresh_token": refresh_token, "token_type": "bearer", "user_id": user.id}

@router.get("/get", response_model= user_response)
def get_user(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Enpoint para recuperar los datos de un usuario autenticado

    Parámetros:
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (session): Sesión de SQLAlchemy para acceder a la base de datos.
            Este parámetro es inyectado automáticamente por la dependencia 'get_db'.
    """
    user = get_user_by_id(db, user_id)
    return user

@router.put("/update", response_model= user_response)
def update(data: user_update, user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para actualizar tanto el avatar como el apodo (nickname) de un usuario autenticado.

    Parámetros:
        data (user_update): Objeto que contiene la información necesaria para actualizar los datos de un usuario
            Incluye: el avatar y el apodo.

    Retorna:
        user_response: Objeto con la informaión actualizada del usuario.
        {
            "email": <email del usuario>,
            "avatar": <nombre del fichero de imagen del avatar>,
            "nickname": <apodo del usuario>
        }
    """
    user = update_user(db= db, user_id= user_id, user_nickname= data.nickname, user_new_avatar= data.avatar)
    return user

@router.put("/change-password")
def change_password(data: change_password_schema, db: Session = Depends(get_db)):
    """
    Endpoint que permite cambiar la contraseña de un usuario.

    Parámetros:
        data (change_password_schema): Objecto que contiene la información necesaria para cambiar la contraseña del usuario.
            Incluye: el email, la contraseña antigua y la contraseña nueva.
        db (session): Sesión de SQLAlchemy para acceder a la base de datos.
            Este parámetro es inyectado automáticamente por la dependencia 'get_db'.

    Retorna:
        dict: Mensaje de confirmación, en caso de cambiarse la contraseña de forma correcta.
            {"message": "Contraseña actualizada correctamente"}

    Lanza:
        HTTPException: Si el email del usuario no está asociada a ninguna cuenta o la antigua contraseña no es válida.
    """
    user = get_user_by_email(db, data.email)
    if not user or not verify(data.old_password, user.hashedPass):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Contraseña incorrecta")

    update_password(db, user, data.new_password)
    return {"message": "Contraseña actualizada correctamente"}

@router.post("/refresh")
def refresh_token(request: refresh_token_request):
    """
    Endpoint para generar un nuevo par de tokens JWT (access y refresh) a partir de un refresh token válido.

    Parámetros:
        request (refresh_token_request): Objeto que contiene enl refresh tóken que se desa validar y usar.

    Retorna:
        dict: Contiene el par de tokens JWT (access y refresh) junto a su tipo de token.
        {
            "access_token": <nuevo token de acceso>,
            "refresh_token": <nuevo token de refresco>,
            "token_type": "bearer"
        }

    Lanza:
        HTTPException: Si el tóken no es válido, ha expirado o no se puede verificar
    """
    payload = verify_token(request.refresh_token)
    
    if not payload:
        raise HTTPException(status_code=401, detail="Refresh token inválido o expirado")

    user_id = payload.get("sub")

    # Generar nuevos tokens
    new_access_token = create_access_token({"sub": user_id})
    new_refresh_token = create_refresh_token({"sub": user_id})

    return {"access_token": new_access_token, "refresh_token": new_refresh_token, "token_type": "bearer"}

@router.post("/verify")
def verifyToken(request: token):
    """
    Endpoint para verificar la validez de un token JWT proporcionado

    Parámetros:
        request (token) Objecto que contiene el token JWT que se desea verificar.

    Retorna:
        bool: True si el token es válido.

    Lanza:
        HTTPException: Si el token no es válido o no se puede verificar.

    """
    verify_token(request.token)
    return True

@router.delete("/delete")
def delete_user(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    """
    Endpoint para eliminar de la aplicación a un usuario autenticado.

    Parámetros:
        user_id (int): Es inyectado automáticamente por 'Depends(get_current_user)', y del cual se obtiene el identificador del usuario.
        db (session): Sesión de SQLAlchemy para acceder a la base de datos.
            Este parámetro es inyectado automáticamente por la dependencia 'get_db'.

    Retorna:
        dict: Mensaje que confirma que el usuario se ha eliminado de forma correcta.
            {"message": "Usuario eliminado correctamente"}
    Lanza:
        HTTPException: En caso de que no se haya podido eliminar el usuario.
    """
    try:
        remove_user(db, user_id)
        return {"message": "Usuario eliminado correctamente"}
    except Exception as e:
        print(e)
        raise HTTPException(status_code = 500, detail=str(e))
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from db.database import get_db
from db.user_crud import get_user_by_email, create_user, update_password, remove_user, get_user_by_id, update_user
from schema import user, change_password_schema, token_response, refresh_token_request, token, login_info, user_response, user_update
from aux_func.auth import get_current_user
from aux_func.auth import create_access_token, create_refresh_token, verify, verify_token


router = APIRouter()

@router.post("/register")
def register(user_data: user, db: Session = Depends(get_db)):
    existing_user = get_user_by_email(db, user_data.email)
    if existing_user:
        raise HTTPException(status_code=400, detail="El usuario ya existe")

    user = create_user(db, user= user_data)
    return {"message": "Usuario creado exitosamente"}

@router.post("/login")
def login(user_data: login_info, db: Session = Depends(get_db)):
    user = get_user_by_email(db, user_data.email)
    if not user or not verify(user_data.password, user.hashedPass):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Credenciales incorrectas")

    access_token = create_access_token({"sub": str(user.id)})
    refresh_token = create_refresh_token({"sub": str(user.id)})
    return {"access_token": access_token, "refresh_token": refresh_token, "token_type": "bearer", "user_id": user.id}

@router.get("/get", response_model= user_response)
def get_user(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    user = get_user_by_id(db, user_id)
    return user

@router.put("/update", response_model= user_response)
def update(user_id = Depends(get_current_user), data = user_update, db: Session = Depends(get_db)):
    user = update_user(db= db, user_id= user_id, user_nickname= data.nickname, user_new_avatar= data.avatar)
    return user

@router.put("/change-password")
def change_password(data: change_password_schema, db: Session = Depends(get_db)):
    user = get_user_by_email(db, data.email)  # Aquí se debe obtener el usuario autenticado
    if not user or not verify(data.old_password, user.hashedPass):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Contraseña incorrecta")

    update_password(db, user, data.new_password)
    return {"message": "Contraseña actualizada correctamente"}

@router.post("/refresh")
def refresh_token(request: refresh_token_request):
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
    verify_token(request.token)
    return True

@router.delete("/delete")
def delete_user(user_id = Depends(get_current_user), db: Session = Depends(get_db)):
    try:
        remove_user(db, user_id)
        return {"message": "Usuario eliminado correctamente"}
    except Exception as e:
        print(e)
        raise HTTPException(status_code = 500, detail=str(e))
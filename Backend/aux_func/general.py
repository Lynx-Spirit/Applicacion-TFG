from fastapi import HTTPException
from aux_func.auth import verify_token

def get_user_id(userToken: str):
    payload = verify_token(userToken)
    user_id = payload.get("sub")

    if user_id is None:
        raise HTTPException(status_code=401, detail= "Token inválido")

    return user_id
from datetime import datetime, timedelta
from fastapi import HTTPException, status
from jose import JWTError, jwt
from config import settings
from passlib.context import CryptContext


SECRET_KEY = settings.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_DURATION = settings.ACCESS_TOKEN_EXPIRE_HOURS
REFRESH_TOKEN_DURATION = settings.REFRESH_TOKEN_EXPIRE_DAYS

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def hash_password(password: str) -> str:
    return pwd_context.hash(password)

def verify(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password,hashed_password)

def create_token(data: dict, secret: str, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, secret, algorithm=ALGORITHM)

def create_access_token(data: dict):
    return create_token(data, SECRET_KEY, timedelta(hours=ACCESS_TOKEN_DURATION))

def create_refresh_token(data: dict):
    return create_token(data, SECRET_KEY, timedelta(days=REFRESH_TOKEN_DURATION))

def verify_token(token: str):
    try:
        # try to decode the token, it will 
        # raise error if the token is not correct
        payload = jwt.decode(token=token,key=SECRET_KEY, algorithms=[ALGORITHM])

        return payload
    except JWTError:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,detail="Could not validate credentials")
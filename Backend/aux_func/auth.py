from datetime import datetime, timedelta
from fastapi import HTTPException, status, Security
from jose import JWTError, jwt
from config import settings
from passlib.context import CryptContext
from fastapi.security import OAuth2PasswordBearer

# Indicamos dónde el cliente debe envíar las credenciales para obtener los tokens
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/login")


SECRET_KEY = settings.SECRET_KEY
ALGORITHM = "HS256"
ACCESS_TOKEN_DURATION = settings.ACCESS_TOKEN_EXPIRE_HOURS
REFRESH_TOKEN_DURATION = settings.REFRESH_TOKEN_EXPIRE_DAYS

# Configuración para hash de las contraseñas
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def hash_password(password: str) -> str:
    """
    Hashea una contraseña en texto plano.

    Parámetros:
        password(str): Texto plano de la contraseña a hashear.

    Retorna:
        str: Contraseña hasheada.
    """
    return pwd_context.hash(password)

def verify(plain_password: str, hashed_password: str) -> bool:
    """
    Verifica si una contraseña en texto plano coincide con una contraseña hasheada.

    Parámetros:
        plain_password (str): Contraseña en texto plano a verificar.
        hashed_password (str): Contraseña hasheada con la que se va a comparar.

    Retorna:
        bool: True si las contraseñas coinciden. False en caso contrario.
    """
    return pwd_context.verify(plain_password,hashed_password)

def create_token(data: dict, secret: str, expires_delta: timedelta) -> str:
    """
    Crea un token JWT codificado con una fecha de expiración.

    Parámetros:
        data(dict): Información que se incluirá en el contenido del token.
        secret(str): Clave secreta utilizada para firmar el token.
        expire_delta (timedelta): Duración de la validez del token.

    Retorna:
        str: El token JWT codificado.
    """
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, secret, algorithm=ALGORITHM)

def create_access_token(data: dict) -> str:
    """
    Crea un token JWT de acceso de una duración limitada en horas.

    Parámetros:
        data (dict):  Información que e incluirá en el contenido del token.
    
    Retorna:
        str: Token de acceso JWT codificado.
    """
    return create_token(data, SECRET_KEY, timedelta(hours=ACCESS_TOKEN_DURATION))

def create_refresh_token(data: dict) -> str:
    """
    Crea un token JWT de refresco de una duración limitada en días.

    Parámetros:
        data (dict):  Información que e incluirá en el contenido del token.
    
    Retorna:
        str: Token de refresco JWT codificado.
    """
    return create_token(data, SECRET_KEY, timedelta(days=REFRESH_TOKEN_DURATION))

def get_user_id(userToken: str) -> int:
    """
    Obtención del id del usuario.

    Parámetros:
        userToken (str): Token JWT del usuario

    Retorna:
        int: Identificador del usuario.

    Lanza:
        HTTPException: Si el token no contiene un identificador de usuario.
    """
    payload = verify_token(userToken)
    user_id = payload.get("sub")

    if user_id is None:
        raise HTTPException(status_code=401, detail="Token inválido")

    return user_id

def verify_token(token: str) -> dict:
    """
    Función para verificar la validez de un token JWT.

    Parámetros:
        token (str): Token JWT del usuario.

    Retorna: 
        dict: Carga útil (payload) del tóken si es válido.

    Lanza:
        HTTPException: Si el tóken no es válido o no puede ser doecodificado, se lanza una excepción indicando que
            las credenciales no pudieron ser validadas.
    """
    try:
        payload = jwt.decode(token=token,key=SECRET_KEY, algorithms=[ALGORITHM])
        
        return payload
    except JWTError:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,detail="Could not validate credentials")
    
def get_current_user(token: str = Security(oauth2_scheme)) -> int:
    """
    Obtiene el identificador del usuario actual a partir del tojen JWT proporcionado.
    Se extrae el id del usuario del token JWT, usando el esquema de seguridad OAuth2.

    Parámetros:
        token (str): Token JWT proporcionado para obtener el identificador del usuario.

    Retorna:
        int: Identificador del usuario extraido en caso de que el token es válido.
    
    Lanza:
        HTTPException: Si del token no se puede extraer el identificador del usuario o este no tiene,
            se lanza un error.
    """
    user_id = get_user_id(token)
    
    if not user_id:
        raise HTTPException(status_code=401, detail="Token inválido o expirado")
    
    return user_id
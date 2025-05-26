from sqlalchemy.orm import Session
from db.campaign_crud import get_campaign_by_code
import random
import string

# Longitud del código de invitación.
CODE_LENGTH = 6
# Tipo de caracteres que va a contenerel código de invitación
CHARACTERS = string.ascii_uppercase + string.digits

def generate_invite_code(db: Session) -> str:
    """
    Genera un código de invitación único y aleatorio con la longitud y tipo de caracteres deseados

    Parámetros:
      db (Session): Sesión de SQLAlchemy para acceder a la base de datos.

    Retorna:
      str: Código de invitación único.
    """
    result: str
    is_unique = False

    while not is_unique:
          code = ''.join(random.choices(CHARACTERS, k=CODE_LENGTH))
          is_unique = get_campaign_by_code(db, code) is None

    return code
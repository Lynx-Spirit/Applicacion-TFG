from sqlalchemy.orm import Session
from db.campaign_crud import get_campaign_by_code
import random
import string

CODE_LENGTH = 6
CHARACTERS = string.ascii_uppercase + string.digits

#El código sería un string de 6 caracteres (solo mayúsculas y números) → XE23YE por ejemplo, luego eso se tendría que validar, en caso de que sea correcto se envía,
#en caso contrario se vuelve a generar el código 
def generate_invite_code(db: Session) -> str:
    result: str
    is_unique = False

    while not is_unique:
          code = ''.join(random.choices(CHARACTERS, k=CODE_LENGTH))
          is_unique = get_campaign_by_code(db, code) is not None

    return code
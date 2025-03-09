from sqlalchemy.orm import Session
from Backend.auth import hash_password
from models import User


def get_user_by_id(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()

def update_password(db: Session, user: User, new_password: str):
    user.hashed_password = hash_password(new_password)
    db.commit()
    db.refresh(user)
    return user

def create_user(db: Session, email: str, password: str):
    hashed_pw = hash_password(password)
    new_user = User(email=email, hashed_password=hashed_pw)
    db.add(new_user)
    db.commit()
    db.refresh(new_user)
    return new_user
from sqlalchemy.orm import Session
from aux_func.auth import hash_password
from db.models import User


def get_user_by_id(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()

def get_users_campaigns(db: Session, user_id: int):
    user = get_user_by_id(db, user_id)
    return user.campaigns

def update_password(db: Session, user_email: str, new_password: str):
    user = get_user_by_email(db= db, email= user_email)

    user.hashed_password = hash_password(new_password)

    db.commit()
    db.refresh(user)

    return user

def create_user(db: Session, email: str, password: str):
    hashed_pw = hash_password(password)

    new_user = User(email=email, hashedPass=hashed_pw)

    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return new_user

def remove_user(db: Session, user_id: int):
    user = get_user_by_id(db, user_id)

    if user:
        db.delete(user)
        db.commit()
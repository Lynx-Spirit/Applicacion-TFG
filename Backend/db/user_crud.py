from sqlalchemy.orm import Session
from aux_func.img_aux import delete
from aux_func.auth import hash_password
from db.models import User
from schema import user


def get_user_by_id(db: Session, user_id: int):
    return db.query(User).filter(User.id == user_id).first()

def get_user_by_email(db: Session, email: str):
    return db.query(User).filter(User.email == email).first()

def get_users_campaigns(db: Session, user_id: int):
    user = get_user_by_id(db, user_id)
    return user.joined_campaigns

def update_password(db: Session, user_email: str, new_password: str):
    user = get_user_by_email(db= db, email= user_email)

    user.hashed_password = hash_password(new_password)

    db.commit()
    db.refresh(user)

    return user

def update_user(db: Session, user_id: int, user_nickname: str = None, user_new_avatar: str = None):
    user = get_user_by_id(db, user_id)

    if user_nickname:
        user.nickname = user_nickname

    if user_new_avatar:
        user.avatar = user_new_avatar

    db.commit()
    db.refresh(user)

    return user

def create_user(db: Session, user: user):
    hashed_pw = hash_password(user.password)

    new_user = User(email= user.email, nickname= user.nickname, hashedPass= hashed_pw, avatar= user.avatar)

    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return new_user

def remove_user(db: Session, user_id: int):
    user = get_user_by_id(db, user_id)

    if user:
        delete(user.avatar_file_name)
        db.delete(user)
        db.commit()
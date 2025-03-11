from pydantic import BaseModel, EmailStr

class User(BaseModel):
    email: EmailStr
    password: str

class ChangePasswordSchema(BaseModel):
    email: EmailStr
    old_password: str
    new_password: str

class Config:
        from_attributes = True
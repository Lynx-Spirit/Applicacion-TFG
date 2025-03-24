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
        
class RefreshTokenRequest(BaseModel):
    refresh_token: str

class TokenResponse(BaseModel):
    access_token: str
    refresh_token: str

class Token(BaseModel):
     token: str
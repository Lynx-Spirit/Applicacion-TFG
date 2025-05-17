from pydantic import BaseModel, EmailStr

class user(BaseModel):
    email: EmailStr
    password: str
    avatar: str
    nickname: str

class user_response(BaseModel):
    id: int
    email: EmailStr
    password: str
    avatar: str
    nickname: str

class login_info(BaseModel):
    email: EmailStr
    password: str

class change_password_schema(BaseModel):
    email: EmailStr
    old_password: str
    new_password: str

class Config:
        from_attributes = True
        
class refresh_token_request(BaseModel):
    refresh_token: str

class token_response(BaseModel):
    access_token: str
    refresh_token: str

class token(BaseModel):
     token: str

class campaign(BaseModel):
     title: str
     description: str
     img_name: str

class campaign_response(BaseModel):
     id: int
     title: str
     description: str
     img_name: str
     invite_code: str
     creator_id: int

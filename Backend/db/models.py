from sqlalchemy import Boolean, Integer, String, ForeignKey, Column, Table
from sqlalchemy.orm import relationship
from database import Base

campaign_invites = Table(
    "campaign_invites",
    Base.metadata,
    Column("campaign_id", Integer, ForeignKey("campaigns.id")),
    Column("user_id", Integer, ForeignKey("users.id"))
)

class User(Base):
    __tablename__ = "users"

    id = Column(Integer,primary_key= True, index= True)
    email = Column(String, index= True)
    hashedPass = Column(String)

    campagins = relationship("User", secondary= campaign_invites, back_populates= "members")

class Campaign(Base):
    __tablename__ = "campaigns"

    id = Column(Integer,primary_key= True, index= True)
    title = Column(String, nullable=False)
    description = Column(String)
    imgURL = Column(String)

    creatorID = Column(Integer,ForeignKey("users.id"))
    members = relationship("User", secondary= campaign_invites, back_populates= "campagins")
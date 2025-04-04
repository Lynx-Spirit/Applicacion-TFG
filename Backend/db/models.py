from sqlalchemy import Boolean, Integer, String, ForeignKey, Column, Table
from sqlalchemy.orm import relationship
from db.database import Base

campaign_invites = Table(
    "campaign_invites",
    Base.metadata,
    Column("campaign_id", Integer, ForeignKey("campaigns.id", ondelete="CASCADE")),
    Column("user_id", Integer, ForeignKey("users.id", ondelete="CASCADE"))
)

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, index=True)
    hashedPass = Column(String)

    campaigns = relationship("Campaign", secondary=campaign_invites, back_populates="members")

class Campaign(Base):
    __tablename__ = "campaigns"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    description = Column(String)
    img_url = Column(String)
    invite_code = Column(String)

    creator_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))
    members = relationship("User", secondary=campaign_invites, back_populates="campaigns")

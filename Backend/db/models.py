from sqlalchemy import Boolean, Integer, String, ForeignKey, Column, Table, UniqueConstraint
from sqlalchemy.orm import relationship
from db.database import Base

# Tabla intermedia para la relación n:m entre las campañas y los usuarios invitados.
campaign_invites = Table(
    "campaign_invites",
    Base.metadata,
    Column("campaign_id", Integer, ForeignKey("campaigns.id", ondelete="CASCADE")),
    Column("user_id", Integer, ForeignKey("users.id", ondelete="CASCADE")),
    UniqueConstraint("campaign_id", "user_id", name="unique_campaign_user")
)

class User(Base):
    """
    Representa a un usuario de la aplicación.

    Atributos:
        id (int): Identificador del usuario.
        email (str): Correo electrónico del usuario.
        nickname (str): Apodo del usuario dentro de la aplicación.
        avatar (str): Nombre de archivo de imagen del avatar.
        hashedPass (str): Contraseña encriptada.

    Relaciones:
        created_campaigns: Campañas creadas por el usuario (como Dungeon Master)
        joined_campaigns:  Camapañas a las que el usuario se ha unido como miembro.
    """
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    email = Column(String, index=True)
    nickname = Column(String)
    avatar = Column(String)
    hashedPass = Column(String)

    created_campaigns = relationship(
        "Campaign",
        cascade="all, delete",
        backref="creator",
        foreign_keys='Campaign.creator_id'
    )  

    joined_campaigns = relationship(
        "Campaign",
        secondary=campaign_invites,
        passive_deletes=True,
        back_populates="members"
    )
    
class Campaign(Base):
    """
    Representa una campaña de la aplicación.

    Atributos:
        id (int): Identificador de la campaña.
        title (str): Nombre de la campaña.
        description (str): Descripción de la campaña.
        img_name (str): Nombre de archivo de imagen de la campaña.
        invite_code (str): Código de invitación de la campaña.

    Relaciones:
        creator_id (int): Identificador del creador de la campaña.
        members: Miembros de la campaña.
    """
    __tablename__ = "campaigns"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    description = Column(String)
    img_name = Column(String)
    invite_code = Column(String)

    creator_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))
    members = relationship(
        "User",
        secondary=campaign_invites,
        passive_deletes=True,
        back_populates="joined_campaigns"
    )
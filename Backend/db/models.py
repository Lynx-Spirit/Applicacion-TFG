from sqlalchemy import Boolean, Integer, String, ForeignKey, Column, Table, UniqueConstraint, Date
from sqlalchemy.orm import relationship
from db.database import Base

# Tabla intermedia para la relación n:m entre las campañas y los usuarios invitados.
campaign_invites = Table(
    "campaign_invites",
    Base.metadata,
    Column("campaign_id", Integer, ForeignKey("campaign.id", ondelete="CASCADE")),
    Column("user_id", Integer, ForeignKey("user.id", ondelete="CASCADE")),
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
    __tablename__ = "user"

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
    __tablename__ = "campaign"

    id = Column(Integer, primary_key=True, index=True)
    title = Column(String, nullable=False)
    description = Column(String)
    img_name = Column(String)
    invite_code = Column(String)

    creator_id = Column(Integer, ForeignKey("user.id", ondelete="CASCADE"))
    members = relationship(
        "User",
        secondary=campaign_invites,
        passive_deletes=True,
        back_populates="joined_campaigns"
    )

class Character(Base):
    """
    Representa un personaje de la aplicación.

    Atributos:
        id (int): Identificador del personaje.
        name (str): Nombre del personaje.
        description (str): Descripción general del personaje
        filename_backstory (str): Nombre del archivo del backstory.
        img_name (str): Nombre del archivo de imagen del personaje.
        visibility (bool): Indica si el personaje está visible al resto de usuarios o no.

    Relaciones:
        campaign_id (int): Identificador de la partida a la que pertenece.
        user_id (int): Ideniticador del usuario que lo ha creado.
    """
    __tablename__ = "character"

    id = Column(Integer, primary_key=True, index=True)
    campaign_id = Column(Integer, ForeignKey("campaign.id", ondelete="CASCADE"))
    user_id = Column(Integer, ForeignKey("user.id", ondelete="CASCADE"))
    name = Column(String)
    description = Column(String)
    filename_backstory = Column(String)
    img_name = Column(String)
    visibility = Column(Boolean)

class Note(Base):
    """
    Representa una nota creada por un usuario dentro de la aplicación.

    Atributos:
        id (int): Identificador de la nota.
        creation_date (date): Fecha de creaión de la nota.
        title (str): Título de la nota.
        file_name (str): Nombre del archivo que contiene toda la info de la nota.
        visibility (bool): Indica si la nota es visible o no al resto de usuarios.

    Relaciones:
        campaign_id (int): Identificador de la partida a la que pertenece.
        user_id (int): Ideniticador del usuario que lo ha creado.
    """
    __tablename__ = "note"
    
    id = Column(Integer, primary_key=True, index=True)
    campaign_id = Column(Integer, ForeignKey("campaign.id", ondelete="CASCADE"))
    user_id = Column(Integer, ForeignKey("user.id", ondelete="SET NULL"), nullable=True)
    creation_date = Column(Date)
    title = Column(String)
    file_name = Column(String)
    visibility = Column(Boolean)
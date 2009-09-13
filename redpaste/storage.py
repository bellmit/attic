from random import sample, randrange
from datetime import datetime
from sqlalchemy import Column, Unicode, UnicodeText, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm.exc import NoResultFound

ID_CHARS = u'abcdefghijkmpqrstuvwxyzABCDEFGHIJKLMNPQRST23456789'
ID_SIZE = 12

def generate_id():
    return ''.join(sample(ID_CHARS, ID_SIZE))

Mapped = declarative_base()

class Paste(Mapped):
    __tablename__ = 'paste'
    
    def __init__(self, author, body, syntax):
        self.author = author
        self.body = body
        self.syntax = syntax
    
    # A generated ID for each paste.
    id = Column(Unicode(ID_SIZE), primary_key=True, default=generate_id)
    # The paste's author.
    author = Column(Unicode, nullable=False)
    # The actual pasted text.
    body = Column(UnicodeText, nullable=False)
    # The paste's language, for syntax highlighting.
    syntax = Column(Unicode, nullable=False)
    # The time at which the paste was created.
    timestamp = Column(DateTime, nullable=False, default=datetime.utcnow)

class PasteStore(object):
    def __init__(self, url):
        self.engine = create_engine(url)
        Mapped.metadata.create_all(self.engine)
        
        self.Session = sessionmaker(
            bind=self.engine,
            expire_on_commit=False
        )
    
    def latest(self, count=10):
        """Retrieves the last ``count`` pastes."""
        session = self.Session()
        try:
            return session.query(Paste).order_by(Paste.timestamp.desc())[0:count]
        finally:
            session.close()
    
    def get(self, id):
        """Retrieves a specific paste."""
        session = self.Session()
        try:
            return session.query(Paste).filter(Paste.id==id).one()
        except NoResultFound:
            raise KeyError, id
        finally:
            session.close()
    
    def new(self, author, body, syntax):
        """Creates and returns a new paste."""
        paste = Paste(author, body, syntax)
        session = self.Session()
        try:
            session.add(paste)
            paste = session.merge(paste)
            session.commit()
            return paste
        finally:
            session.close()

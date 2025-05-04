import os
from config import settings
from uuid import uuid4

async def save(file, upload_folder = settings.UPLOAD_FOLDER):
    os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)
    
    image_filename = f"{uuid4()}{os.path.splitext(file.filename)[1]}"
    image_path = os.path.join(upload_folder, image_filename)

    with open(image_path, "wb") as image_file:
        image_file.write(await file.read())

    return image_filename

def delete(image_filename, upload_folder = settings.UPLOAD_FOLDER):
    path = os.path.join(upload_folder,image_filename)
    
    if os.path.exists(path):
        os.remove(path)
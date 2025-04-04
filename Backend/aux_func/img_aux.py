import os
from urllib.parse import urlparse
from config import settings
from uuid import uuid4
from aux_func.ip import get_local_ip

def save(file, upload_folder = settings.UPLOAD_FOLDER, ip = get_local_ip()):
    os.makedirs(settings.UPLOAD_FOLDER, exist_ok=True)
    
    image_filename = f"{uuid4()}{os.path.splitext(file.filename)[1]}"
    image_path = os.path.join(upload_folder, image_filename)

    with open(image_path, "wb") as image_file:
        image_file.write(file.read())

    image_url = f"http://{ip}:8000/images/{image_filename}"
    return image_url

def delete(url, upload_folder = settings.UPLOAD_FOLDER):
    parsed_url = urlparse(url)
    image_filename = parsed_url.path.split("/")[-1]
    path = os.path.join(upload_folder,image_filename)
    
    if os.path.exists(path):
        os.remove(path)
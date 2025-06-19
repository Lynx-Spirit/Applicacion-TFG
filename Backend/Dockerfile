FROM nvidia/cuda:12.3.2-devel-ubuntu20.04

# Instalar Python 3.11
RUN apt-get update && \
    apt-get install -y python3.11 python3-pip python3-dev

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos de la aplicación
COPY . .

# Instalar dependencias
RUN pip install --no-cache-dir -r Requirements.txt

# Exponer el puerto 8000
EXPOSE 8000

# Ejecutar FastAPI con Uvicorn
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]

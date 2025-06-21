FROM nvidia/cuda:12.1.1-cudnn8-runtime-ubuntu22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV PYTHONUNBUFFERED=1

# 1. Instala Python 3.11 y herramientas del sistema
RUN apt-get update && apt-get install -y \
    software-properties-common \
    curl \
    git \
    ffmpeg \
    libsndfile1 \
    libflac8 libogg0 libvorbis0a libvorbisenc2 libvorbisfile3 \
    build-essential \
    ca-certificates && \
    add-apt-repository ppa:deadsnakes/ppa && \
    apt-get update && apt-get install -y \
    python3.11 \
    python3.11-venv \
    python3.11-distutils && \
    curl -sS https://bootstrap.pypa.io/get-pip.py | python3.11 && \
    ln -s /usr/bin/python3.11 /usr/bin/python && \
    ln -s /usr/local/bin/pip3.11 /usr/bin/pip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 2. Establece el directorio de trabajo
WORKDIR /app

# 3. Copia el proyecto
COPY . ./

# 4. Instala dependencias desde Requirements.txt (incluye PyTorch con CUDA)
RUN pip install --upgrade pip && \
    pip install -r Requirements.txt

# 5. Expone el puerto y ejecuta la aplicación
EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]

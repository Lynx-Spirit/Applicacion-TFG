# Etapa 1: Builder
FROM python:3.11-slim as builder

ENV DEBIAN_FRONTEND=noninteractive
ENV PYTHONUNBUFFERED=1

# Instala dependencias del sistema necesarias para compilar y audio
RUN apt-get update && apt-get install -y \
    git \
    curl \
    ca-certificates \
    ffmpeg \
    libsndfile1 \
    build-essential \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Establece directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto
COPY . ./

# Actualiza pip
RUN pip install --upgrade pip

# Establece un directorio para instalaciones limpias
ENV PYTHONUSERBASE=/install

# Instala PyTorch + Hugging Face Transformers, Datasets con soporte para audio y Accelerate
RUN pip install --user \
    torch==2.2.2 torchvision==0.17.2 torchaudio==2.2.2 --index-url https://download.pytorch.org/whl/cpu && \
    pip install --user transformers datasets[audio] accelerate

# Instala las dependencias propias de la app
RUN pip install --user -r Requirements.txt

# Etapa 2: Imagen final liviana
FROM python:3.11-slim as final

ENV DEBIAN_FRONTEND=noninteractive
ENV PYTHONUNBUFFERED=1

# Instala solo las librerías necesarias para ejecutar
RUN apt-get update && apt-get install -y \
    ffmpeg \
    libsndfile1 \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Crea el directorio de trabajo
WORKDIR /app

# Copia el entorno de instalación del builder
COPY --from=builder /install /usr/local

# Copia tu aplicación
COPY . ./

# Exponer el puerto usado por FastAPI
EXPOSE 8000

# Comando para ejecutar el servidor
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]

import torch
from transformers import AutoModelForSpeechSeq2Seq, AutoProcessor, pipeline

class WhisperTranscriber:
    """
    Clase que carga una vez whisper para así tardar menos en tema de cargar el modelo.
    """
    def __init__(self):
        model_id = "openai/whisper-large-v3-turbo"
        device = "cuda:0" if torch.cuda.is_available() else "cpu"
        torch_dtype = torch.float16 if torch.cuda.is_available() else torch.float32

        self.model = AutoModelForSpeechSeq2Seq.from_pretrained(
            model_id,
            torch_dtype=torch_dtype,
            low_cpu_mem_usage=True,
            use_safetensors=True
        ).to(device)

        self.processor = AutoProcessor.from_pretrained(model_id)

        self.pipe = pipeline(
            "automatic-speech-recognition",
            model=self.model,
            tokenizer=self.processor.tokenizer,
            feature_extractor=self.processor.feature_extractor,
            torch_dtype=torch_dtype,
            device=device
        )

    def transcribe(self, audio_path: str):
        with torch.inference_mode():
            result = self.pipe(audio_path, return_timestamps=True)
        
        return result

whisper_instance = WhisperTranscriber()

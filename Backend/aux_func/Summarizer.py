from gpt4all import GPT4All

class Summarizer:
    """
    Carga el modelo de lenguaje para hacer resúmenes.
    """
    def __init__(self, model_path: str):
        self.model = GPT4All(model_path, device='cuda')

    def summarize(self, text: str,) -> str:
        """
        Genera un resumen del texto proporcionado.

        Parámetros:
            text: Texto a resumir

        Retrona
            Texto resumido.
        """
        prompt = (
        "Devuelve solo el resumen puro y directo del siguiente texto, sin comentarios ni explicaciones:\n\n"
        f"{text}\n\n"
        "Resumen:"
        )  


        result = self.model.generate(prompt, max_tokens=512)
        return result

# Singleton
summarizer_instance = Summarizer("qwen2.5-coder-7b-instruct-q4_0.gguf")

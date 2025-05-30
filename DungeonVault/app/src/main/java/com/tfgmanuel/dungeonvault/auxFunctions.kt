package com.tfgmanuel.dungeonvault

/**
 * Limita la frase original, cortándola y añadiéndole '...' al final.
 *
 * @param originalPhrase La frase original que se quiere limitar.
 * @param max El número máximo de caracteres permitidos para la frase truncada (por defecto 200).
 *
 * @return La frase truncada seguida de '...', si la frase original excede el límite.
 */
fun trimPhrase(originalPhrase: String, max: Int = 200): String {
    if (originalPhrase.length <= 200) {
        return originalPhrase
    } else {
        val shortPhrase = originalPhrase.substring(0,200)
        val lastSpace = shortPhrase.lastIndexOf(' ')

        return if (lastSpace != -1) {
            shortPhrase.substring(0,lastSpace) + " ..."
        } else {
            "$shortPhrase ..."
        }
    }
}
package com.tfgmanuel.dungeonvault

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
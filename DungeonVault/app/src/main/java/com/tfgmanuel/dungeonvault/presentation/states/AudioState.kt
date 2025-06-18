package com.tfgmanuel.dungeonvault.presentation.states

data class AudioState(
    var isRecording: Boolean = false,
    var elapsedTime: Long = 0L
)

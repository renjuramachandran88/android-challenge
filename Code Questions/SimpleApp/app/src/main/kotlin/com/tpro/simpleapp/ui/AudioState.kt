package com.tpro.simpleapp.ui

import com.tpro.simpleapp.data.entity.AudioResponse

data class AudioState(
    val audioList: List<AudioResponse> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

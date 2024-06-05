package com.tpro.simpleapp.data.entity

import com.google.gson.annotations.SerializedName

data class AudioResponse(
    val id: Int,
    val name: String,
    @SerializedName("audio_url") val audioUrl: String,
)

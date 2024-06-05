package com.tpro.simpleapp.data.remote

import com.tpro.simpleapp.data.entity.AudioResponse
import retrofit2.Response
import retrofit2.http.GET

interface AudioRemoteService {
    @GET("dev")
    suspend fun getAudioResponse(): Response<List<AudioResponse>>
}
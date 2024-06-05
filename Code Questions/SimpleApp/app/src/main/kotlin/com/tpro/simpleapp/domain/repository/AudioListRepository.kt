package com.tpro.simpleapp.domain.repository

import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.domain.Result
import kotlinx.coroutines.flow.Flow

interface AudioListRepository {
    suspend fun getAudioList(): Flow<Result<List<AudioResponse>>>
}
package com.tpro.simpleapp.data.repository

import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.data.remote.AudioRemoteService
import com.tpro.simpleapp.domain.Result
import com.tpro.simpleapp.domain.repository.AudioListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AudioListRepositoryImpl @Inject constructor(
    private val audioRemoteService: AudioRemoteService
) : AudioListRepository {

    override suspend fun getAudioList(): Flow<Result<List<AudioResponse>>> {
        return flow {
            try {
                val response = audioRemoteService.getAudioResponse()
                if (response.isSuccessful) {
                    emit(Result.Success(response.body() ?: emptyList()))
                } else {
                    emit(Result.Error(Exception("Network Error: ${response.code()}")))
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }
}
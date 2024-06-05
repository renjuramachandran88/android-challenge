package com.tpro.simpleapp.domain.usecase

import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.domain.Result
import com.tpro.simpleapp.domain.repository.AudioListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAudioListUseCase @Inject constructor(
    private val audioListRepository: AudioListRepository
) {
    suspend operator fun invoke(): Flow<Result<List<AudioResponse>>> =
        audioListRepository.getAudioList()
}
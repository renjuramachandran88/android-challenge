package com.tpro.simpleapp.di

import com.tpro.simpleapp.data.remote.AudioRemoteService
import com.tpro.simpleapp.data.repository.AudioListRepositoryImpl
import com.tpro.simpleapp.domain.repository.AudioListRepository
import com.tpro.simpleapp.domain.usecase.GetAudioListUseCase
import dagger.Module
import dagger.Provides

@Module
object RepositoryModule {
    @Provides
    fun providedAudioListRepository(
        audioRemoteService: AudioRemoteService
    ): AudioListRepository = AudioListRepositoryImpl(audioRemoteService)

    @Provides
    fun providesAudioListUsecase(
        audioListRepository: AudioListRepository
    ) = GetAudioListUseCase(audioListRepository)
}
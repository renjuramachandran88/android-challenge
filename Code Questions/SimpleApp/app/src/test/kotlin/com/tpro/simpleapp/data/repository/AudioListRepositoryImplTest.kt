package com.tpro.simpleapp.data.repository

import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.data.remote.AudioRemoteService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


@ExperimentalCoroutinesApi
class AudioListRepositoryImplTest {

    private lateinit var audioRemoteService: AudioRemoteService
    private lateinit var audioListRepository: AudioListRepositoryImpl

    @Before
    fun setUp() {
        audioRemoteService = mock(AudioRemoteService::class.java)
        audioListRepository = AudioListRepositoryImpl(audioRemoteService)
    }

    @Test
    fun `getAudioList should emit success result when response is successful`() = runTest {
        // Given
        val audioResponse = listOf(
            AudioResponse(
                id = 1,
                name = "Test Audio",
                audioUrl = "http://test.com/audio.mp3"
            )
        )
        val response = Response.success(audioResponse)
        whenever(audioRemoteService.getAudioResponse()).thenReturn(response)

        // When
        val result = audioListRepository.getAudioList().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is com.tpro.simpleapp.domain.Result.Success)
        assertEquals(audioResponse, (result[0] as com.tpro.simpleapp.domain.Result.Success).data)
    }

    @Test
    fun `getAudioList should emit error result when response is unsuccessful`() = runTest {
        // Given
        val response = Response.error<List<AudioResponse>>(500, mock(ResponseBody::class.java))
        whenever(audioRemoteService.getAudioResponse()).thenReturn(response)

        // When
        val result = audioListRepository.getAudioList().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is com.tpro.simpleapp.domain.Result.Error)
        assertEquals(
            "Network Error: 500",
            (result[0] as com.tpro.simpleapp.domain.Result.Error).exception?.message
        )
    }

    @Test
    fun `getAudioList should emit error result when exception is thrown`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(audioRemoteService.getAudioResponse()).thenThrow(exception)

        // When
        val result = audioListRepository.getAudioList().toList()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0] is com.tpro.simpleapp.domain.Result.Error)
        assertEquals(exception, (result[0] as com.tpro.simpleapp.domain.Result.Error).exception)
    }
}
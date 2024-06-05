package com.tpro.simpleapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tpro.simpleapp.data.entity.AudioResponse
import com.tpro.simpleapp.domain.Result
import com.tpro.simpleapp.domain.usecase.GetAudioListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AudioListViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getAudioListUseCase: GetAudioListUseCase

    private lateinit var viewModel: AudioListViewModel

    @Mock
    private lateinit var observer: Observer<AudioState>

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AudioListViewModel(getAudioListUseCase)
        viewModel.audioLivedata.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.audioLivedata.removeObserver(observer)
    }

    @Test
    fun `fetch should emit loading then success`() = runTest {
        // Given
        val audioList =
            listOf(AudioResponse(id = 1, name = "Test Audio", audioUrl = "http://test.com/audio.mp3"))
        whenever(getAudioListUseCase()).thenReturn(flow {
            emit(Result.Success(audioList))
        })

        // When
        viewModel.fetch()

        // Then
        val inOrder = inOrder(observer)
        inOrder.verify(observer).onChanged(AudioState(isLoading = true))
        inOrder.verify(observer)
            .onChanged(AudioState(audioList = audioList, isLoading = false, errorMessage = ""))
    }

    @Test
    fun `fetch should emit loading then error`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(getAudioListUseCase()).thenReturn(flow {
            emit(Result.Error(Exception(errorMessage)))
        })

        // When
        viewModel.fetch()

        // Then
        verify(observer).onChanged(AudioState(isLoading = true))
        advanceUntilIdle()
        verify(observer).onChanged(
            AudioState(
                audioList = emptyList(),
                isLoading = false,
                errorMessage = "Error"
            )
        )
    }
}
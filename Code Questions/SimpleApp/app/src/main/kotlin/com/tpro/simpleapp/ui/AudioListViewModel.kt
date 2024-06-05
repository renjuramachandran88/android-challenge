package com.tpro.simpleapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpro.simpleapp.domain.Result
import com.tpro.simpleapp.domain.usecase.GetAudioListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioListViewModel @Inject constructor(
    private val audioListUseCase: GetAudioListUseCase
) : ViewModel() {

    private var _audioLivedata = MutableLiveData(AudioState())
    val audioLivedata = _audioLivedata


    fun fetch() {
        viewModelScope.launch {
            _audioLivedata.value = _audioLivedata.value?.copy(isLoading = true)
            audioListUseCase()
                .flowOn(Dispatchers.IO)
                .catch {
                    _audioLivedata.value = _audioLivedata.value?.copy(
                        audioList = emptyList(),
                        isLoading = false,
                        errorMessage = it.localizedMessage ?: "Error"
                    )

                }.collect {
                    when (it) {
                        is Result.Success -> {
                            _audioLivedata.value = _audioLivedata.value?.copy(
                                audioList = it.data,
                                isLoading = false,
                                errorMessage = ""
                            )
                        }

                        is Result.Error -> {
                            _audioLivedata.value = _audioLivedata.value?.copy(
                                audioList = emptyList(),
                                isLoading = false,
                                errorMessage = it.errorMessage ?: "Error"
                            )
                        }
                    }
                }
        }
    }

}
package com.tpro.simpleapp.di;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider;
import com.tpro.simpleapp.ui.AudioListViewModel

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AudioListViewModel::class)
    abstract fun bindAudioViewModel(viewModel: AudioListViewModel): ViewModel
}

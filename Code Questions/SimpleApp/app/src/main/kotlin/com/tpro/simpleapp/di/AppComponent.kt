package com.tpro.simpleapp.di

import android.app.Application
import com.tpro.simpleapp.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton
@Singleton
@Component(
        NetworkModule::class,
        RepositoryModule::class,
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }


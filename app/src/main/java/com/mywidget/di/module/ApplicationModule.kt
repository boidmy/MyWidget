package com.mywidget.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideViewModelFactory() : ViewModelProvider.NewInstanceFactory {
        return ViewModelProvider.NewInstanceFactory()
    }

}
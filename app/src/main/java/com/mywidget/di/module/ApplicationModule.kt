package com.mywidget.di.module

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.mywidget.viewModel.MyViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    fun provideViewModelFactory(application: Application) : ViewModelProvider.NewInstanceFactory {
        return MyViewModelFactory(application)
    }

}
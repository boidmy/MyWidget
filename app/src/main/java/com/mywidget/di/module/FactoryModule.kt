package com.mywidget.di.module

import androidx.lifecycle.ViewModelProvider
import com.mywidget.viewModel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
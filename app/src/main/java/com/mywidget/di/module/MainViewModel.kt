package com.mywidget.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.UserViewModel
import com.mywidget.viewModel.ViewModelFactory
import com.mywidget.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class MainViewModel {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainFragmentViewModel): ViewModel
}
package com.mywidget.di.module

import androidx.lifecycle.ViewModel
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    internal abstract fun bindViewModel(viewModel: MainFragmentViewModel): ViewModel
}
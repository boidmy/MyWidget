package com.mywidget.ui.main

import androidx.lifecycle.ViewModel
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.di.custom.ViewModelKey
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
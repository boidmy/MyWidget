package com.mywidget.di.module

import androidx.lifecycle.ViewModel
import com.mywidget.viewModel.UserViewModel
import com.mywidget.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UserViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    internal abstract fun bindViewModel(viewModel: UserViewModel): ViewModel
}
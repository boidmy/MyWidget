package com.mywidget.ui.signup

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignUpViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun bindViewModel(viewModel: SignUpViewModel): ViewModel
}
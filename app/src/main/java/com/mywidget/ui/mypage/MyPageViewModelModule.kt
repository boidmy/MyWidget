package com.mywidget.ui.mypage

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MyPageViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyPageViewModel::class)
    internal abstract fun bindViewModel(viewModel: MyPageViewModel): ViewModel
}
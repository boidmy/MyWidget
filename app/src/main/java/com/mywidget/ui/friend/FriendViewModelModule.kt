package com.mywidget.ui.friend

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FriendViewModel::class)
    internal abstract fun bindViewModel(viewModel: FriendViewModel): ViewModel
}
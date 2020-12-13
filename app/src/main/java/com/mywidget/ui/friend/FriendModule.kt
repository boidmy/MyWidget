package com.mywidget.ui.friend

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import com.mywidget.ui.chat.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendModule {

    @Binds
    @IntoMap
    @ViewModelKey(FriendViewModel::class)
    internal abstract fun bindViewModel(viewModel: FriendViewModel): ViewModel
}
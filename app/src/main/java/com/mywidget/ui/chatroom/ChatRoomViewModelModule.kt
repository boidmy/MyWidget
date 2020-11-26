package com.mywidget.ui.chatroom

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatRoomViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatRoomViewModel::class)
    internal abstract fun bindViewModel(viewModel: ChatRoomViewModel): ViewModel
}
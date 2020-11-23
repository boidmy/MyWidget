package com.mywidget.chat.chatting

import androidx.lifecycle.ViewModel
import com.mywidget.chat.viewmodel.ChatViewModel
import com.mywidget.viewModel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindViewModel(viewModel: ChatViewModel): ViewModel
}
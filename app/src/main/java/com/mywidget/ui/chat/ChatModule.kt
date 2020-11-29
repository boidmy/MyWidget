package com.mywidget.ui.chat

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ChatModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindViewModel(viewModel: ChatViewModel): ViewModel
}
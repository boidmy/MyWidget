package com.mywidget.ui.chatinvite

import androidx.lifecycle.ViewModel
import com.mywidget.di.custom.ViewModelKey
import com.mywidget.ui.chat.ChatViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatInviteModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatInviteViewModel::class)
    internal abstract fun bindViewModel(viewModel: ChatInviteViewModel): ViewModel
}
package com.mywidget.di.compoenet

import com.mywidget.chat.chatting.ChatActivity
import com.mywidget.chat.chatting.ChatModule
import dagger.Subcomponent

@Subcomponent(modules = [ChatModule::class])
interface ChattingActivityComponent {

    fun inject(chatActivity: ChatActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ChattingActivityComponent
    }
}
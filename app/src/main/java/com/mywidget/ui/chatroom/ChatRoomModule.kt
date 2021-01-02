package com.mywidget.ui.chatroom

import android.app.Dialog
import android.view.LayoutInflater
import com.mywidget.R
import com.mywidget.databinding.ChatCreateRoomBinding
import com.mywidget.databinding.DeleteConfirmDialogChatRoomBinding
import dagger.Module
import dagger.Provides

@Module
class ChatRoomModule {

    @Provides
    fun provideDialog(activity: ChatRoomActivity): Dialog {
        return Dialog(activity, R.style.CustomDialogTheme)
    }

    @Provides
    fun provideCreateRoomDialogBinding(activity: ChatRoomActivity): ChatCreateRoomBinding {
        return ChatCreateRoomBinding.inflate(LayoutInflater.from(activity))
    }

    @Provides
    fun provideDeleteRoomDialogBinding(activity: ChatRoomActivity): DeleteConfirmDialogChatRoomBinding {
        return DeleteConfirmDialogChatRoomBinding.inflate(LayoutInflater.from(activity))
    }
}
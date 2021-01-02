package com.mywidget.ui.chat

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.mywidget.R
import com.mywidget.di.custom.ViewModelKey
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.support.DaggerAppCompatActivity
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
abstract class ChatModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun bindViewModel(viewModel: ChatViewModel): ViewModel

    companion object {
        @Provides
        @Named("inChatPush")
        fun provideSharedPreferences(activity: ChatActivity) : SharedPreferences {
            return activity.getSharedPreferences(activity.getString(R.string.inChatPush),
                DaggerAppCompatActivity.MODE_PRIVATE
            )
        }

        @Provides
        fun provideSharedEdit(@Named("inChatPush") sharedPreferences: SharedPreferences)
                : SharedPreferences.Editor {
            return sharedPreferences.edit()
        }
    }
}
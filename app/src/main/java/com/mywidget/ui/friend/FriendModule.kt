package com.mywidget.ui.friend

import android.app.Dialog
import android.view.LayoutInflater
import com.mywidget.R
import com.mywidget.databinding.DeleteConfirmDialogFriendBinding
import com.mywidget.databinding.FriendAddDialogBinding
import com.mywidget.databinding.FriendUpdateDialogBinding
import dagger.Module
import dagger.Provides

@Module
class FriendModule {

    @Provides
    fun provideDialog(activity: FriendActivity): Dialog {
        return Dialog(activity, R.style.CustomDialogTheme)
    }

    @Provides
    fun provideFriendAddDialogBinding(activity: FriendActivity): FriendAddDialogBinding {
        return FriendAddDialogBinding.inflate(LayoutInflater.from(activity))
    }

    @Provides
    fun provideDeleteDialogBinding(activity: FriendActivity): DeleteConfirmDialogFriendBinding {
        return DeleteConfirmDialogFriendBinding.inflate(LayoutInflater.from(activity))
    }

    @Provides
    fun provideFriendUpdateDialogBinding(activity: FriendActivity): FriendUpdateDialogBinding {
        return FriendUpdateDialogBinding.inflate(LayoutInflater.from(activity))
    }
}
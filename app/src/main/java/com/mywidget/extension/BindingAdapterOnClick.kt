package com.mywidget.extension

import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.google.android.gms.common.SignInButton
import com.mywidget.R
import com.mywidget.data.Constants.Companion.REQUEST_RC_SIGN_IN
import com.mywidget.data.model.FriendModel
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chat.ChatViewModel
import com.mywidget.ui.chatinvite.ChatInviteActivity
import com.mywidget.ui.chatinvite.ChatInviteViewModel
import com.mywidget.ui.chatroom.ChatRoomViewModel
import com.mywidget.ui.friend.FriendViewModel
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.login.signup.SignUpActivity
import util.Util
import util.Util.click
import util.Util.toast

@BindingAdapter("email", "password", "confirmPassword", "missId", "missPassword", "activity")
fun firebaseSignUp(
    button: Button,
    editEmail: EditText,
    editPassword: EditText,
    editConfirmPassword: EditText,
    missId: TextView,
    missPassword: TextView,
    activity: SignUpActivity
) {
    button.click {
        val vinputPassword: String = editPassword.text.toString()
        val confirmPassword: String = editConfirmPassword.text.toString()
        val email: String = editEmail.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(confirmPassword)
            || TextUtils.isEmpty(vinputPassword)) {
            missId.visibility = View.VISIBLE
            return@click
        }

        if(vinputPassword != confirmPassword) {
            missPassword.visibility = View.VISIBLE
            return@click
        }

        activity.singUpFirebase(email, vinputPassword)
    }
}

@BindingAdapter("email", "password", "activity")
fun loginUser(button: Button, email: EditText, password: EditText, activity: LoginActivity) {
    button.run {
        click {
            val emailVal = email.text.toString()
            val passwordVal = password.text.toString()
            when {
                emailVal.isEmpty() -> context.toast(context.getString(R.string.inputEmail))
                passwordVal.isEmpty() -> context.toast(context.getString(R.string.inputPassword))
                else -> activity.signInPassword(emailVal, passwordVal)
            }
        }
    }
}

@BindingAdapter("activity")
fun loginGoogle(button: View, activity: LoginActivity) {
    button.click {
        val signInIntent= activity.mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, REQUEST_RC_SIGN_IN)
    }
}

@BindingAdapter("chatRoomViewModel", "roomSubject")
fun createRoom(imageView: ImageView, viewModel: ChatRoomViewModel, editText: EditText) {
    imageView.run {
        click {
            if (editText.text.toString().isEmpty()) {
                context.toast(context.getString(R.string.inputRoomTitle))
            } else {
                viewModel.myId?.let {
                    viewModel.createRoom(it, editText.text.toString())
                    viewModel.dialogVisibility(false)
                }
            }
        }
    }
}

@BindingAdapter("isSelected", "viewModel")
fun onClickSelected(view: View, data: FriendModel, viewModel: ChatInviteViewModel) {
    view.click {
        viewModel.friendList.value?.map {
            if (it.email == data.email) {
                it.copy(selector = it.selector.not())
            } else {
                it
            }
        }?.let {
            viewModel.setFriendList(it)
        }
    }
}

@BindingAdapter("favoritesAdd", "viewModel")
fun setFavoritesAdd(button: Button, email: String, viewModel: FriendViewModel) {
    button.click {
        viewModel.setFavorites(email, !button.isSelected)
    }
}
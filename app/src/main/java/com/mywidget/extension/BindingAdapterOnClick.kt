package com.mywidget.extension

import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.google.android.gms.common.SignInButton
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
    button.setOnClickListener {
        val vinputPassword: String = editPassword.text.toString()
        val confirmPassword: String = editConfirmPassword.text.toString()
        val email: String = editEmail.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(confirmPassword)
            || TextUtils.isEmpty(vinputPassword)) {
            missId.visibility = View.VISIBLE
            return@setOnClickListener
        }

        if(vinputPassword != confirmPassword) {
            missPassword.visibility = View.VISIBLE
            return@setOnClickListener
        }

        activity.singUpFirebase(email, vinputPassword)
    }
}

@BindingAdapter("email", "password", "activity")
fun loginUser(button: Button, email: EditText, password: EditText, activity: LoginActivity) {
    button.setOnClickListener {
        val emailVal = email.text.toString()
        val passwordVal = password.text.toString()
        when {
            emailVal.isEmpty() -> {
                Toast.makeText(button.context, "이메일을 입력해주세요", Toast.LENGTH_LONG).show()
            }
            passwordVal.isEmpty() -> {
                Toast.makeText(button.context, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
            }
            else -> {
                activity.signInPassword(emailVal, passwordVal)
            }
        }
    }
}

@BindingAdapter("activity")
fun loginGoogle(button: View, activity: LoginActivity) {
    button.setOnClickListener {
        val signInIntent= activity.mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, activity.RC_SIGN_IN)
    }
}

@BindingAdapter("inviteUserAdd", "editText", "activity")
fun inviteUserAdd(imageView: ImageView, viewModel: ChatInviteViewModel
                  , editText: EditText, activity: ChatInviteActivity) {
    imageView.setOnClickListener {
        val userEmail = editText.text.toString()
        if(userEmail.isEmpty()) {
            imageView.context.toast("이메일을 입력하세요")
        } else {
            viewModel.userExistenceChk(userEmail)
        }
    }
}

@BindingAdapter("chatRoomViewModel", "roomSubject")
fun createRoom(imageView: ImageView, viewModel: ChatRoomViewModel, editText: EditText) {
    imageView.setOnClickListener {
        if(editText.text.toString().isEmpty()) {
            Toast.makeText(imageView.context, "방 제목을 입력해 주세요", Toast.LENGTH_LONG).show()
        } else {
            viewModel.myId?.let {
                viewModel.createRoom(it, editText.text.toString())
                viewModel.dialogVisibility(false)
            }
        }
    }
}

@BindingAdapter("isSelected", "viewModel")
fun onClickSelected(view: View, position: Int, viewModel: ChatInviteViewModel) {
    view.setOnClickListener {
        val array = viewModel.friendList.value
        val selector = array?.get(position)?.selector ?: true
        array?.get(position)?.selector = !selector
        array?.let {
            viewModel.setFriendList(it)
        }
    }
}

@BindingAdapter("favoritesAdd", "viewModel")
fun setFavoritesAdd(button: Button, email: String, viewModel: FriendViewModel) {
    button.setOnClickListener {
        viewModel.setFavorites(email, !button.isSelected)
    }
}
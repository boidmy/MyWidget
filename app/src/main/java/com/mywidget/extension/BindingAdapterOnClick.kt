package com.mywidget.extension

import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.mywidget.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_signup.*

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
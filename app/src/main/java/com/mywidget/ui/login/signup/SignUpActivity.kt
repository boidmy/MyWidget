package com.mywidget.ui.login.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.mywidget.R
import com.mywidget.databinding.ActivitySignupBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import util.Util
import util.Util.firebaseAuthException
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> { factory }
    val PASSWORD_SIGN_IN = 102

    override val layout: Int
        get() = R.layout.activity_signup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        bindView()
    }

    fun bindView() {
        viewModel.signUpComplete.observe(this, Observer {
            if(it) {
                val intent = Intent()
                intent.putExtra("result", true)
                setResult(PASSWORD_SIGN_IN, intent)
                finish()
            }
        })
    }

    fun singUpFirebase(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                user?.email?.let { userVal ->
                    viewModel.singUpFirebase(userVal, user.uid, nicknameEdit.text.toString())
                }
            } else {
                task.exception?.let {
                    this firebaseAuthException (task.exception as FirebaseAuthException).errorCode
                }
            }
        }
    }
}
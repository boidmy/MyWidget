package com.mywidget.ui.login.signup

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
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_signup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        bindView()
    }

    fun bindView() {
        viewModel.setSignUpComplete()
        viewModel.signUpComplete.observe(this, Observer {
            if(it) finish()
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
                    Util.firebaseAuthException((task.exception as FirebaseAuthException).errorCode, this)
                }
            }
        }
    }
}
package com.mywidget.ui.login.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivitySignupBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.login.LoginViewModel
import java.lang.Exception
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<LoginViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_signup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
    }

    fun singUpFirebase(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                user?.email?.let { userVal ->
                    viewModel.singUpFirebase(userVal, user.uid)
                }
            } else {
                task.exception?.let {
                    emailException(it)
                }
            }
        }
        viewModel.signUpComplete.observe(this, Observer {
            if(it) finish()
        })
    }

    private fun emailException(exception: Exception) {
        makeToast(exception.message?:"잠시후 다시 시도해 주세요")
    }

    private fun makeToast(output: String) {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show()
    }
}
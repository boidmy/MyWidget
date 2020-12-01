package com.mywidget.ui.signup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivitySignupBinding
import com.mywidget.ui.base.BaseActivity
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<SignUpViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_signup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this
    }

    fun singUpFirebase(email: String, password: String) {
        viewModel.singUpFirebase(email, password)
        viewModel.signUpComplete.observe(this, Observer {
            if(it) finish()
        })
    }
}
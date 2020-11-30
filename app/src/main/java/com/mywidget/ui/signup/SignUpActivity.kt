package com.mywidget.ui.signup

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivitySignupBinding
import com.mywidget.ui.base.BaseActivity
import javax.inject.Inject

class SignUpActivity : BaseActivity<ActivitySignupBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this, factory).get(SignUpViewModel::class.java)}

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
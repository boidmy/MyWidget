package com.mywidget.ui.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityMypageBinding
import com.mywidget.ui.base.BaseActivity
import util.observe
import javax.inject.Inject

class MyPageActivity : BaseActivity<ActivityMypageBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MyPageViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_mypage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setObserve()
    }

    fun bind() {
        binding.vm = viewModel
        with(viewModel) {
            setMyId(loginEmail())
            selectMyNickName()
            resetConfirm()
        }
    }

    private fun setObserve() {
        with(viewModel) {
            observe(updateConfirm, ::updateComplete)
        }
    }

    private fun updateComplete(confirm: Boolean) {
        if (confirm) finish()
    }
}
package com.mywidget.ui.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mywidget.MainApplication
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<D : ViewDataBinding> : DaggerAppCompatActivity() {

    protected lateinit var binding: D
    protected abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
    }

    fun loginEmail(): String {
        return MainApplication.INSTANSE.loginEmail()
    }
}
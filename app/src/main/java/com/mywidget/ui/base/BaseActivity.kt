package com.mywidget.ui.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.auth.FirebaseUser
import com.mywidget.MainApplication
import com.mywidget.R
import dagger.android.support.DaggerAppCompatActivity
import util.Util.toast

abstract class BaseActivity<D : ViewDataBinding> : DaggerAppCompatActivity() {

    protected lateinit var binding: D
    protected abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
    }

    fun loginEmail() = authUser()?.email ?: ""

    fun authUser(): FirebaseUser? = MainApplication.INSTANSE.authUser()

    fun loginChkToast(): Boolean {
        return if (loginEmail().isEmpty()) {
            toast("로그인 후 이용해 주세요")
            false
        } else {
            true
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
    }
}
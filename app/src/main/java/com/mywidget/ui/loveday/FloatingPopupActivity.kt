package com.mywidget.ui.loveday

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.data.*
import com.mywidget.databinding.LovedayPopup2Binding
import dagger.android.support.DaggerAppCompatActivity
import util.Util.toast
import javax.inject.Inject
import javax.inject.Named

class FloatingPopupActivity : DaggerAppCompatActivity() {

    private lateinit var binding: LovedayPopup2Binding
    @Inject @Named("floatingOpen") lateinit var openAnimation: Animation
    @Inject @Named("floatingClose") lateinit var closeAnimation: Animation
    @Inject @Named("rotateOpen") lateinit var startRotateAnimation: Animation
    @Inject @Named("rotateClose") lateinit var endRotateAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LovedayPopup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        activationAnimation(true)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
    }

    private fun activationAnimation(flag: Boolean) {
        val anim = if (flag) openAnimation else closeAnimation
        val rotateAnim = if (flag) startRotateAnimation else endRotateAnimation
        binding.chatContainer.startAnimation(anim)
        binding.loveDayContainer.startAnimation(anim)
        binding.conditionContainer.startAnimation(anim)
        binding.memoContainer.startAnimation(anim)
        binding.floatingBtn.startAnimation(rotateAnim)
    }

    fun onClickFloatingBtn(v: View) {
        when (v.id) {
            R.id.memoContainer -> {
                Intent().apply {
                    putExtra(RESULT, MEMO)
                }.run {
                    setResult(RESULT_OK, this)
                }
            }
            R.id.conditionContainer -> {
                Intent().apply {
                    putExtra(RESULT, CONDITION)
                }.run {
                    setResult(RESULT_OK, this)
                }
            }
            R.id.loveDayContainer -> {
                Intent().apply {
                    putExtra(RESULT, D_DAY)
                }.run {
                    setResult(RESULT_OK, this)
                }
            }
            R.id.chatContainer -> {
                if (loginEmail().isEmpty()) {
                    toast(getString(R.string.afterLogin))
                    return
                }
                Intent().apply {
                    putExtra(RESULT, CHAT)
                }.run {
                    setResult(RESULT_OK, this)
                }
            }
        }
        activationAnimation(false)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 150)
    }

    private fun loginEmail(): String {
        return MainApplication.INSTANSE.authUser()?.email ?: ""
    }
}
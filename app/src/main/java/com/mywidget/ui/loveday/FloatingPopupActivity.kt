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
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.loveday_popup2.*
import util.Util.toast
import javax.inject.Inject
import javax.inject.Named

class FloatingPopupActivity : DaggerAppCompatActivity() {

    @Inject
    @Named("floatingOpen")
    lateinit var openAnimation: Animation
    @Inject
    @Named("floatingClose")
    lateinit var closeAnimation: Animation
    @Inject
    @Named("rotateOpen")
    lateinit var startRotateAnimation: Animation
    @Inject
    @Named("rotateClose")
    lateinit var endRotateAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loveday_popup2)

        activationAnimation(true)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
    }

    private fun activationAnimation(flag: Boolean) {
        val anim = if (flag) openAnimation else closeAnimation
        val rotateAnim = if (flag) startRotateAnimation else endRotateAnimation
        chatContainer.startAnimation(anim)
        loveDayContainer.startAnimation(anim)
        conditionContainer.startAnimation(anim)
        memoContainer.startAnimation(anim)
        floatingBtn.startAnimation(rotateAnim)
    }

    fun onClickFloatingBtn(v: View) {
        when (v.id) {
            R.id.memoContainer -> {
                val intent = Intent()
                intent.putExtra("result", "memo")
                setResult(RESULT_OK, intent)
            }
            R.id.conditionContainer -> {
                val intent = Intent()
                intent.putExtra("result", "condition")
                setResult(RESULT_OK, intent)
            }
            R.id.loveDayContainer -> {
                val intent = Intent()
                intent.putExtra("result", "dDay")
                setResult(RESULT_OK, intent)
            }
            R.id.chatContainer -> {
                if (loginEmail().isEmpty()) {
                    this.toast("로그인 후 이용해 주세요")
                    return
                }
                val intent = Intent()
                intent.putExtra("result", "chat")
                setResult(RESULT_OK, intent)
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
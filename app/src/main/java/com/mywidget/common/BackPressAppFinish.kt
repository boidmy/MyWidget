package com.mywidget.common

import android.app.Activity
import android.widget.Toast

class BackPressAppFinish(context: Activity) {

    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null

    private var activity = context

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
            toast?.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(activity, "뒤로버튼 한번 더 누르면 종료돼용", Toast.LENGTH_SHORT)
        toast?.show()
    }
}
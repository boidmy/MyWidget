package com.mywidget

import android.app.Activity
import android.widget.Toast

class BackPressAppFinish(context: Activity) {

    var backKeyPressdTime: Long = 0
    var toast: Toast? = null

    private var activity = context

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressdTime + 2000) {
            backKeyPressdTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressdTime + 2000) {
            activity.finish()
            toast?.cancel()
        }
    }

    fun showGuide() {
        toast = Toast.makeText(activity, "뒤로버튼 한번 더 누르면 종료돼용", Toast.LENGTH_SHORT)
        toast?.show()
    }
}
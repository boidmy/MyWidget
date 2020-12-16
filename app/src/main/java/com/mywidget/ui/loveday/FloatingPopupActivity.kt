package com.mywidget.ui.loveday

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.lmemo.view.LMemoActivity
import util.Util.toast

class FloatingPopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.loveday_popup)
    }

    fun onClickFloatingBtn(v: View) {
        when (v.id){
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
                if(loginEmail().isEmpty()) {
                    this.toast("로그인 후 이용해 주세요")
                    return
                }
                val intent = Intent(this, ChatRoomActivity::class.java)
                startActivity(intent)
            }
        }
        finish()
    }

    private fun loginEmail(): String {
        return MainApplication.INSTANSE.loginEmail()
    }
}
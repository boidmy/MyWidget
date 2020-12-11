package com.mywidget.ui.loveday

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.lmemo.view.LMemoActivity
import kotlinx.android.synthetic.main.loveday_popup.*
import util.Util.toast

class FloatingPopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.loveday_popup)

        conditionContainer.setOnClickListener(clickListener)
        loveDayContainer.setOnClickListener(clickListener)
        chatContainer.setOnClickListener(clickListener)
        smart_talk_floating_popup_dim_layout.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v.id){
            R.id.conditionContainer -> {
                val intent = Intent(this, LMemoActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.loveDayContainer -> {
                val intent = Intent()
                intent.putExtra("result", "dday")
                setResult(RESULT_OK, intent)
                finish()
            }
            R.id.chatContainer -> {
                if(loginEmail().isEmpty()) {
                    this.toast("로그인 후 이용해 주세요")
                    finish()
                    return@OnClickListener
                }
                val intent = Intent(this, ChatRoomActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.smart_talk_floating_popup_dim_layout -> finish()
        }
    }

    fun loginEmail(): String {
        return MainApplication.INSTANSE.loginEmail()
    }
}
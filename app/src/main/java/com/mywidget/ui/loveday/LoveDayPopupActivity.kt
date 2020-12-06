package com.mywidget.ui.loveday

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mywidget.R
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.lmemo.view.LMemoActivity
import kotlinx.android.synthetic.main.loveday_popup.*

class LoveDayPopupActivity : Activity() {

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
                val intent = Intent(this, ChatRoomActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.smart_talk_floating_popup_dim_layout -> finish()
        }
    }
}
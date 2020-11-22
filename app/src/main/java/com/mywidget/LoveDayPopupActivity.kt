package com.mywidget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.mywidget.chat.waiting.WaitingRoomActivity
import com.mywidget.lmemo.view.LMemoActivity
import kotlinx.android.synthetic.main.loveday_popup.*

class LoveDayPopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.loveday_popup)

        condition.setOnClickListener(clickListener)
        dDay.setOnClickListener(clickListener)
        chatBtn.setOnClickListener(clickListener)
        smart_talk_floating_popup_dim_layout.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v.id){
            R.id.condition -> {
                val intent = Intent(this, LMemoActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.dDay -> {
                val intent = Intent()
                intent.putExtra("result", "dday")
                setResult(RESULT_OK, intent)
                finish()
            }
            R.id.chatBtn -> {
                val intent = Intent(this, WaitingRoomActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.smart_talk_floating_popup_dim_layout -> finish()
        }
    }
}
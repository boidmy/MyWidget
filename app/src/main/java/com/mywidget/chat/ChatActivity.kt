package com.mywidget.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mywidget.R
import kotlinx.android.synthetic.main.activity_chatting.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        chat_rv.adapter = ChatAdapter()

        send_btn.setOnClickListener {  }
    }
}
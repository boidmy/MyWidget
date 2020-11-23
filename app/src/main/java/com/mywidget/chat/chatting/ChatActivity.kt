package com.mywidget.chat.chatting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DatabaseReference
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.chat.viewmodel.ChatViewModel
import com.mywidget.databinding.ActivityChattingBinding
import com.mywidget.view.BaseActivity
import kotlinx.android.synthetic.main.activity_chatting.*
import javax.inject.Inject

class ChatActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, factory).get(ChatViewModel::class.java)
    }
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userAct: GoogleSignInAccount?
            by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    override val layout: Int
        get() = R.layout.activity_chatting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).getApplicationCompoenet()
            .chattingActivityComponent().create().inject(this)

        val roomKey = intent.getStringExtra("roomKey")
        chat_rv.adapter = ChatAdapter()
        var mEmail: List<String>? = null

        userAct?.email?.let {
            mEmail = it.split("@")
            if(mEmail != null && roomKey != null) {
                viewModel.selectChat(mEmail!![0], roomKey)
            }
        }
        sendBtn.setOnClickListener {
            roomKey?.let {
                val result: HashMap<String, String> = hashMapOf()
                result["message"] = chatEdit.text.toString()
                result["id"] = mEmail!![0]
                roomRef.child(mEmail!![0]).child(it)
                    .child("message").push().setValue(result)
            }
        }
    }
}
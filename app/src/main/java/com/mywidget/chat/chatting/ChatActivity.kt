package com.mywidget.chat.chatting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        ViewModelProvider(this, factory).get(ChatViewModel::class.java) }
    private val userAct: GoogleSignInAccount?
            by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    override val layout: Int
        get() = R.layout.activity_chatting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).getApplicationCompoenet()
            .chattingActivityComponent().create().inject(this)

        binding.viewModel = viewModel
        bind()
    }

    fun bind() {
        binding.chatRv.adapter = ChatAdapter(viewModel)
        val roomKey = intent.getStringExtra("roomKey")?:""
        val master = intent.getStringExtra("master")?:""
        viewModel.userId(userAct?.email?:"")
        viewModel.selectChat(master, roomKey)
        binding.sendBtn.setOnClickListener {
            viewModel.insertChat(master, roomKey,
                userAct?.email?:"", chatEdit.text.toString())
            binding.chatEdit.text.clear()
        }
        binding.inviteUser.setOnClickListener {
            viewModel.inviteUser()
        }
    }
}
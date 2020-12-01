package com.mywidget.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DatabaseReference
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.databinding.ActivityChattingBinding
import com.mywidget.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chatting.*
import util.ItemDecoration
import javax.inject.Inject

class ChatActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatViewModel> { factory }
    private val userAct: GoogleSignInAccount?
            by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    override val layout: Int
        get() = R.layout.activity_chatting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        bind()
    }

    private fun bind() {
        val adapter = ChatAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.chatRv.adapter = adapter
        binding.chatRv.addItemDecoration(ItemDecoration(this, 10))

        val roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        viewModel.userId(userAct?.email?:"")
        viewModel.selectChat(roomDataModel)
        binding.sendBtn.setOnClickListener {
            viewModel.insertChat(userAct?.email?:"", chatEdit.text.toString())
            binding.chatEdit.text.clear()
        }
    }
}
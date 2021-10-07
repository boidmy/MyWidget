package com.mywidget.ui.chatinvite

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.ActivityChatInviteBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatinvite.recyclerview.ChatInviteAdapter
import javax.inject.Inject

class ChatInviteActivity : BaseActivity<ActivityChatInviteBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatInviteViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_chat_invite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    fun bind() {
        binding.viewModel = viewModel
        viewModel.myId(loginEmail())
        viewModel.selectFriendList()
        binding.inviteRv.adapter = ChatInviteAdapter(viewModel)

        val roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        viewModel.setChatRoomInformation(roomDataModel)
    }

    fun onclickInvite(v: View) {
        viewModel.inviteUserArray()
        finish()
    }
}
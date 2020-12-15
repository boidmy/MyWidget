package com.mywidget.ui.chatinvite

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.ActivityChatInviteBinding
import com.mywidget.databinding.ChatInviteUserAddBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatinvite.recyclerview.ChatInviteRecyclerView
import com.mywidget.ui.friend.FriendViewModel
import util.Util.toast
import javax.inject.Inject

class ChatInviteActivity : BaseActivity<ActivityChatInviteBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatInviteViewModel> { factory }
    private val inviteUserAddBinding by lazy {
        ChatInviteUserAddBinding.inflate(LayoutInflater.from(this))
    }
    private val inviteDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.activity_chat_invite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        inviteDialog()
    }

    fun bindView() {
        binding.viewModel = viewModel
        viewModel.myId(loginEmail())
        viewModel.selectFriendList()
        binding.inviteRv.adapter = ChatInviteRecyclerView(viewModel)

        val roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        viewModel.setChatRoomInformation(roomDataModel)
    }

    private fun inviteDialog() {
        inviteDialog.setContentView(inviteUserAddBinding.root)
        inviteUserAddBinding.viewModel = viewModel
        inviteUserAddBinding.activity = this
        viewModel.inviteUserExistence()
        viewModel.inviteDialogVisibility()
        viewModel.inviteDialogVisibility.observe(this, Observer {
            if (it) inviteDialog.show()
            else {
                inviteUserAddBinding.chatUserEmailEdit.text = null
                inviteDialog.dismiss()
            }
        })
        viewModel.inviteUserExistence.observe(this, Observer {
            val userEmail = inviteUserAddBinding.chatUserEmailEdit.text.toString()
            if (it) {
                viewModel.inviteUser(userEmail)
                finish()
            } else {
                this.toast("이메일을 다시 확인해주세요")
            }
        })
    }

    fun onclickInvite(v: View) {
        viewModel.inviteUserArray()
        finish()
    }
}
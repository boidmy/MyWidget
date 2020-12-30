package com.mywidget.ui.chatroom

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityChatRoomBinding
import com.mywidget.databinding.ChatCreateRoomBinding
import com.mywidget.databinding.DeleteConfirmDialogChatRoomBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatroom.recyclerview.ChatRoomRecyclerView
import javax.inject.Inject

class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatRoomViewModel> { factory }
    private val dialogBinding
            by lazy { ChatCreateRoomBinding.inflate(LayoutInflater.from(this)) }
    private val createRoomDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }
    private val deleteDialogBinding by lazy {
        DeleteConfirmDialogChatRoomBinding.inflate(LayoutInflater.from(this)) }
    private val deleteDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.activity_chat_room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        deleteDialog()
    }

    private fun bind() {
        binding.viewModel = viewModel
        binding.watingRoomRv.adapter = ChatRoomRecyclerView(viewModel)

        with(loginEmail()) {
            viewModel.selectFriendList(this)
            viewModel.selectRoomList(this)
            viewModel.myId = this
        }

        createRoomDialog()
    }

    private fun createRoomDialog() {
        dialogBinding.viewModel = viewModel
        dialogBinding.id = loginEmail()
        createRoomDialog.setContentView(dialogBinding.root)
        viewModel.isDialogVisibility.observe(this, Observer {
            if(it) createRoomDialog.show()
            else createRoomDialog.dismiss()
        })
    }

    fun openCreateRoomDialog(v: View) {
        viewModel.dialogVisibility(true)
        dialogBinding.chatUserEmailEdit.text = null
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
        viewModel.deleteDialogVisibility.observe(this, Observer {
            if (it) deleteDialog.show()
            else deleteDialog.dismiss()
        })
        viewModel.deleteRoom.observe(this, Observer {
            deleteDialogBinding.data = it
            viewModel.deleteDialogVisibility(true)
        })
    }
}
package com.mywidget.ui.chatroom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.ActivityChatRoomBinding
import com.mywidget.databinding.ChatCreateRoomBinding
import com.mywidget.databinding.DeleteConfirmDialogChatRoomBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chatroom.recyclerview.ChatRoomRecyclerView
import javax.inject.Inject

class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var dialogBinding: ChatCreateRoomBinding
    @Inject lateinit var createRoomDialog: Dialog
    @Inject lateinit var deleteDialogBinding: DeleteConfirmDialogChatRoomBinding
    @Inject lateinit var deleteDialog: Dialog
    private val viewModel by viewModels<ChatRoomViewModel> { factory }

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

        observer()
        createRoomDialog()
        chatInPush()
    }

    private fun observer() {
        viewModel.resetLastMessage()
        viewModel.roomLastMessage.observe(this, Observer {
            binding.watingRoomRv.adapter?.notifyDataSetChanged()
        })

        viewModel.enterRoom.observe(this, Observer {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("data", it)
            intent.putExtra("friendMap", viewModel.friendHashMap.value)
            this.startActivity(intent)
        })

        viewModel.roomList.observe(this, Observer {
            viewModel.selectLastMessage(it)
        })
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

    private fun chatInPush() {
        val intent = intent
        val extras = intent.extras
        val chatArray = extras?.getStringArray(getString(R.string.runChat))
        chatArray?.let {
            //푸쉬 진입시 친구 목록을 불러온 후 채팅방 진입
            viewModel.friendHashMap.observe(this, Observer {
                val roomData = RoomDataModel("", chatArray[0], chatArray[1])
                viewModel.enterRoom(roomData)
            })
        }
    }
}
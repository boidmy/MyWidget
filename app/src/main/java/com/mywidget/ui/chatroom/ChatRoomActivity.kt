package com.mywidget.ui.chatroom

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.data.*
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.ActivityChatRoomBinding
import com.mywidget.databinding.ChatCreateRoomBinding
import com.mywidget.databinding.DeleteConfirmDialogChatRoomBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chatroom.recyclerview.ChatRoomAdapter
import util.LandingRouter.move
import util.Util.click
import util.observe
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
        binding.apply {
            vm = viewModel
            watingRoomRv.adapter = ChatRoomAdapter(viewModel)
        }

        with(viewModel) {
            selectFriendList(loginEmail())
            selectRoomList(loginEmail())
            this
        }.apply {
            myId = loginEmail()
        }

        setObserve()
        createRoomDialog()
        chatInPush()
    }

    private fun setObserve() {
        with(viewModel) {
            resetLastMessage()
            observe(roomLastMessage, ::roomRefresh)
            observe(enterRoom, ::inChatRoom)
            observe(roomList, ::roomListAfterMessage)
            observe(isDialogVisibility, ::createRoomVisibility)
            observe(deleteDialogVisibility, ::deletePopVisibility)
            observe(deleteRoom, ::deleteChatRoom)
        }
    }

    private fun createRoomDialog() {
        dialogBinding.apply {
            vm = viewModel
            id = loginEmail()
            createRoomDialog.setContentView(root)
        }
    }

    fun openCreateRoomDialog(v: View) {
        viewModel.dialogVisibility(true)
        dialogBinding.chatUserEmailEdit.text = null
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
    }

    private fun chatInPush() {
        intent.getBundleExtra(BUNDLE)?.run {
            getStringArray(RUN_CHAT)?.let { item ->
                viewModel.friendHashMap.observe(this@ChatRoomActivity, Observer {
                    viewModel.enterRoom(RoomDataModel("", item[0], item[1]))
                })
            }
        }
    }

    private fun roomRefresh(data: List<ChatDataModel>) {
        binding.watingRoomRv.adapter?.notifyDataSetChanged()
    }

    private fun inChatRoom(data: RoomDataModel) {
        Bundle().apply {
            putSerializable(INTENT_EXTRA_DATA, data)
            putSerializable(
                INTENT_EXTRA_FRIEND_DATA,
                viewModel.friendHashMap.value as HashMap
            )
        }.run {
            move(RouterEvent(type = Landing.CHAT, data = this))
        }
    }

    private fun roomListAfterMessage(data: List<RoomDataModel>) {
        viewModel.selectLastMessage(data)
    }

    private fun createRoomVisibility(flag: Boolean) {
        if (flag) createRoomDialog.show()
        else createRoomDialog.dismiss()
    }

    private fun deletePopVisibility(flag: Boolean) {
        if (flag) deleteDialog.show()
        else deleteDialog.dismiss()
    }

    private fun deleteChatRoom(data: RoomDataModel) {
        deleteDialogBinding.data = data
        viewModel.deleteDialogVisibility(true)
    }
}
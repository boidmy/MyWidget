package com.mywidget.ui.chatroom

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mywidget.R
import com.mywidget.databinding.ActivityWatingRoomBinding
import com.mywidget.databinding.ChatCreateRoomBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatroom.recyclerview.ChatRoomRecyclerView
import kotlinx.android.synthetic.main.activity_wating_room.*
import javax.inject.Inject

class ChatRoomActivity : BaseActivity<ActivityWatingRoomBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatRoomViewModel> { factory }
    private val dialogBinding by lazy { ChatCreateRoomBinding.inflate(LayoutInflater.from(this)) }
    private val createRoomDialog by lazy { Dialog(this) }

    override val layout: Int
        get() = R.layout.activity_wating_room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    private fun bind() {
        binding.viewModel = viewModel
        binding.watingRoomRv.adapter = ChatRoomRecyclerView(viewModel)

        with(loginEmail()) {
            viewModel.selectRoomList(this)
            viewModel.myId = this
        }

        createRoom.setOnClickListener(onClickCreateRoom)
        createRoomDialog()
    }

    private fun createRoomDialog() {
        dialogBinding.viewModel = viewModel
        dialogBinding.id = loginEmail()
        createRoomDialog.setContentView(dialogBinding.root)
    }

    private val onClickCreateRoom = View.OnClickListener {
        viewModel.isDialogVisibility.value = true
        viewModel.isDialogVisibility.observe(this, Observer {
            if(it) createRoomDialog.show()
            else createRoomDialog.dismiss()
        })
    }
}
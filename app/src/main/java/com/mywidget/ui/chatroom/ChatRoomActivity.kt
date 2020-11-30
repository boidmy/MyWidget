package com.mywidget.ui.chatroom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
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

    private val userAct: GoogleSignInAccount? by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: ChatRoomViewModel by lazy {
        ViewModelProvider(this, factory).get(ChatRoomViewModel::class.java)}

    override val layout: Int
        get() = R.layout.activity_wating_room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    private fun bind() {
        binding.viewModel = viewModel
        binding.watingRoomRv.adapter = ChatRoomRecyclerView(viewModel)
        userAct?.email?.let {
            viewModel.selectRoomList(it.substring(0, it.indexOf('@')))
        }
        createRoom.setOnClickListener {
            /*val email = userAct?.email
            email?.let { viewModel.createRoom(it) }*/

            val binding: ChatCreateRoomBinding = ChatCreateRoomBinding.inflate(LayoutInflater.from(this))
            binding.viewModel = viewModel
            binding.id = userAct?.email
            val dialog = Dialog(this)
            dialog.setContentView(binding.root)
            dialog.show()

        }
    }
}
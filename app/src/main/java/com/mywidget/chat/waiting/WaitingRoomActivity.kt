package com.mywidget.chat.waiting

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mywidget.R
import com.mywidget.chat.waiting.adapter.WatingRoomAdapter
import com.mywidget.chat.viewmodel.WatingRoomViewModel
import com.mywidget.databinding.ActivityWatingRoomBinding
import com.mywidget.view.BaseActivity
import kotlinx.android.synthetic.main.activity_wating_room.*
import javax.inject.Inject

class WaitingRoomActivity : BaseActivity<ActivityWatingRoomBinding>() {

    private val userAct: GoogleSignInAccount? by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: WatingRoomViewModel by lazy {
        ViewModelProvider(this, factory).get(WatingRoomViewModel::class.java)
    }

    override val layout: Int
        get() = R.layout.activity_wating_room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    fun bind() {
        binding.viewModel = viewModel
        binding.watingRoomRv.adapter = WatingRoomAdapter(viewModel)
        userAct?.email?.let {
            viewModel.selectRoomList(it.substring(0, it.indexOf('@')))
        }
        createRoom.setOnClickListener {
            val email = userAct?.email
            email?.let { viewModel.createRoom(it) }
        }
    }
}
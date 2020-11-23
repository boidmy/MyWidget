package com.mywidget.chat.waiting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.*
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.chat.waiting.adapter.WatingRoomAdapter
import com.mywidget.chat.waiting.viewmodel.WatingRoomViewModel
import com.mywidget.databinding.ActivityWatingRoomBinding
import com.mywidget.view.BaseActivity
import kotlinx.android.synthetic.main.activity_wating_room.*
import kotlinx.android.synthetic.main.layout_title.*
import javax.inject.Inject
import javax.inject.Named

class WaitingRoomActivity : BaseActivity<ActivityWatingRoomBinding>() {

    @Inject lateinit var database: DatabaseReference
    private val userAct: GoogleSignInAccount? by lazy { GoogleSignIn.getLastSignedInAccount(this) }
    @Inject lateinit var factory: ViewModelProvider.Factory
    val viewModel: WatingRoomViewModel by lazy {
        ViewModelProvider(this, factory).get(WatingRoomViewModel::class.java)
    }

    override val layout: Int
        get() = R.layout.activity_wating_room

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).getApplicationCompoenet()
            .watingActivityComponent().create().inject(this)
        binding.viewModel = viewModel
        binding.watingRoomRv.layoutManager = LinearLayoutManager(binding.root.context)
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
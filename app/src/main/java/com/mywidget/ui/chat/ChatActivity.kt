package com.mywidget.ui.chat

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.ActivityChattingBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.ui.chat.recyclerview.UserListRecyclerView
import com.mywidget.ui.chatinvite.ChatInviteActivity
import kotlinx.android.synthetic.main.activity_chatting.*
import util.ItemDecoration
import util.Util
import javax.inject.Inject


class ChatActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var preferencesEditor: SharedPreferences.Editor
    private val viewModel by viewModels<ChatViewModel> { factory }
    lateinit var roomDataModel: RoomDataModel
    lateinit var friendHashMap: Map<String, String>

    override val layout: Int
        get() = R.layout.activity_chatting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        bind()
        setRoomSharedPreferences()
    }

    override fun onPause() {
        super.onPause()
        preferencesEditor.clear()
        preferencesEditor.apply()
    }

    private fun init() {
        roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        friendHashMap = intent.getSerializableExtra("friendMap") as Map<String, String>

        with(viewModel) {
            setFriendHashMap(friendHashMap)
            setRoomData(roomDataModel)
            myId(loginEmail())
            setInviteDatabaseMap()
            inviteUserList()
            getListChat()
        }
    }

    private fun bind() {
        binding.apply {
            vm = viewModel
            val adapter = ChatAdapter(viewModel)
            chatRv.adapter = adapter
            chatRv.addItemDecoration(ItemDecoration(10))
            drawerUserListRv.adapter = UserListRecyclerView(viewModel)

            chatRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val last = chatRv.layoutManager?.itemCount?:0
                    if (!recyclerView.canScrollVertically(-1)) {
                        //loadMore
                        viewModel.chatLoadMore(last)
                    }
                }
            })
        }
    }

    override fun onBackPressed() {
        if (binding.chatDrawLayout.isDrawerOpen(GravityCompat.END)) {
            binding.chatDrawLayout.closeDrawer(GravityCompat.END)
            return
        }
        finish()
    }

    fun onClickSendMessage(v: View) {
        viewModel.insertChat(loginEmail(), chatEdit.text.toString())
        binding.chatEdit.text.clear()
    }

    fun onClickOpenDrawer(v: View) {
        Util.downKeyboard(this, chatEdit)
        binding.chatDrawLayout.openDrawer(GravityCompat.END)
    }

    fun onClickInviteUser(v: View) {
        val intent = Intent(this, ChatInviteActivity::class.java)
        intent.putExtra("data", roomDataModel)
        startActivity(intent)
    }

    //사용자가 채팅방에 들어와있으면 push 를 받지 않기 위한 용도
    private fun setRoomSharedPreferences() {
        preferencesEditor.putString(getString(R.string.roomKey), roomDataModel.roomKey)
        preferencesEditor.apply()
    }
}
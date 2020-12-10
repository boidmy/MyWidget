package com.mywidget.ui.chat

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.databinding.ActivityChattingBinding
import com.mywidget.databinding.ChatInviteUserAddBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chat.recyclerview.UserListRecyclerView
import kotlinx.android.synthetic.main.activity_chatting.*
import util.ItemDecoration
import util.Util.toast
import javax.inject.Inject

class ChatActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatViewModel> { factory }
    private val inviteUserAddBinding by lazy {
        ChatInviteUserAddBinding.inflate(LayoutInflater.from(this)) }
    private var inviteDialog: Dialog? = null

    override val layout: Int
        get() = R.layout.activity_chatting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        bind()
        inviteDialog()
    }

    private fun bind() {
        val adapter = ChatAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.chatRv.adapter = adapter
        binding.chatRv.addItemDecoration(ItemDecoration(this, 10))
        binding.drawerUserListRv.adapter = UserListRecyclerView(viewModel)

        val roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        viewModel.userId(loginEmail())
        viewModel.getListChat(roomDataModel)
        viewModel.inviteUserList()

        binding.chatRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val last = binding.chatRv.layoutManager?.itemCount?:0
                if (!recyclerView.canScrollVertically(-1)) {
                    //loadMore
                    viewModel.chatLoadMore(last)
                }
            }
        })
    }

    fun onClickSendMessage(v: View) {
        viewModel.insertChat(loginEmail(), chatEdit.text.toString())
        binding.chatEdit.text.clear()
    }

    private fun inviteDialog() {
        inviteDialog = Dialog(this)
        inviteDialog?.setContentView(inviteUserAddBinding.root)
        inviteUserAddBinding.viewModel = viewModel
        inviteUserAddBinding.activity = this
        viewModel.inviteUserExistence()
        viewModel.inviteDialogVisibility()
        viewModel.inviteDialogVisibility.observe(this, Observer {
            if(it) inviteDialog?.show()
            else {
                inviteUserAddBinding.chatUserEmailEdit.text = null
                inviteDialog?.dismiss()
            }
        })
        viewModel.inviteUserExistence.observe(this, Observer {
            val userEmail = inviteUserAddBinding.chatUserEmailEdit.text.toString()
            if(it) {
                viewModel.inviteUser(userEmail)
                this.toast(userEmail+"님을 초대했습니다")
            } else {
                this.toast("이메일을 다시 확인해주세요")
            }
        })
    }

    fun onClickOpenDrawer(v: View) {
        binding.chatDrawLayout.openDrawer(GravityCompat.END)
    }

    fun onClickInviteUser(v: View) {
        viewModel.inviteDialogShow()
        binding.chatDrawLayout.closeDrawer(GravityCompat.END)
    }
}
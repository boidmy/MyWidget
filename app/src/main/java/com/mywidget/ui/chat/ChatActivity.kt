package com.mywidget.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DatabaseReference
import com.mywidget.R
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.databinding.ActivityChattingBinding
import com.mywidget.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_chatting.*
import util.ItemDecoration
import javax.inject.Inject

class ChatActivity : BaseActivity<ActivityChattingBinding>() {
    @Inject lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<ChatViewModel> { factory }
    override val layout: Int
        get() = R.layout.activity_chatting
    private var chatListCount = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        bind()
    }

    private fun bind() {
        val adapter = ChatAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.chatRv.adapter = adapter
        binding.chatRv.addItemDecoration(ItemDecoration(this, 10))

        val roomDataModel = intent.getSerializableExtra("data") as RoomDataModel
        viewModel.userId(loginEmail())
        viewModel.getListChat(roomDataModel)

        binding.chatRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val last = binding.chatRv.layoutManager?.itemCount?:0
                if (!recyclerView.canScrollVertically(-1)) {
                    //loadMore
                    viewModel.chatLoadMore(last)
                    chatListCount+=20
                }
            }
        })
    }
//recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);
    fun onClickSendMessage(v: View) {
        viewModel.insertChat(loginEmail(), chatEdit.text.toString())
        binding.chatEdit.text.clear()
    }
}
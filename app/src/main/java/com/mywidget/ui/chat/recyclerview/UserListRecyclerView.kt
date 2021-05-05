package com.mywidget.ui.chat.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.model.ChatInviteModel
import com.mywidget.databinding.ChatDrawerUserItemBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.chat.ChatViewModel

class UserListRecyclerView(val viewModel: ChatViewModel) :
    RecyclerView.Adapter<UserListViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    private fun currentList(): MutableList<DiffUtilDataInterface> = diffUtil.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bindView(currentList()[position] as ChatInviteModel)
    }

    fun setData(data: List<ChatInviteModel>) {
        val item: MutableList<ChatInviteModel> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilDataInterface>)
    }
}

class UserListViewHolder(parent: ViewGroup) :
    ViewHolderBase<ChatDrawerUserItemBinding>(parent, R.layout.chat_drawer_user_item) {

    fun bindView(data: ChatInviteModel?) {
        binding.email = data?.email
    }
}
package com.mywidget.ui.chatinvite.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.common.DiffUtilSelectorCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.Interface.DiffUtilSelectorDataInterface
import com.mywidget.data.model.FriendModel
import com.mywidget.databinding.ChatInviteItemBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.chatinvite.ChatInviteViewModel

class ChatInviteAdapter(val viewModel: ChatInviteViewModel)
    : RecyclerView.Adapter<ChatInviteViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, DiffUtilSelectorCallBack)

    private fun currentList(): MutableList<DiffUtilSelectorDataInterface> = diffUtil.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInviteViewHolder {
        return ChatInviteViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: ChatInviteViewHolder, position: Int) {
        holder.bindView(viewModel, position, currentList()[position] as FriendModel)
    }

    fun setData(data: List<FriendModel>) {
        val item: MutableList<FriendModel> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilSelectorDataInterface>)
    }
}

class ChatInviteViewHolder(parent: ViewGroup)
    : ViewHolderBase<ChatInviteItemBinding>(parent, R.layout.chat_invite_item) {

    fun bindView(viewModel: ChatInviteViewModel, position: Int, data: FriendModel) {
        binding.viewModel = viewModel
        binding.position = position
        binding.data = data
        binding.checkBtn.isSelected = data.selector
        binding.executePendingBindings()
    }
}
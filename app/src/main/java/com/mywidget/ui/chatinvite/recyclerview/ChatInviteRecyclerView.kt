package com.mywidget.ui.chatinvite.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.databinding.ChatInviteItemBinding
import com.mywidget.ui.chatinvite.ChatInviteViewModel

class ChatInviteRecyclerView(val viewModel: ChatInviteViewModel)
    : RecyclerView.Adapter<ChatInviteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatInviteViewHolder {
        val bind = ChatInviteItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatInviteViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.friendList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChatInviteViewHolder, position: Int) {
        holder.bindView(viewModel, position)
    }
}

class ChatInviteViewHolder(val binding: ChatInviteItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(viewModel: ChatInviteViewModel, position: Int) {
        binding.viewModel = viewModel
        binding.position = position
        binding.checkBtn.isSelected = viewModel.friendList.value?.get(position)?.selector ?: false
    }
}
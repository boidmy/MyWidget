package com.mywidget.ui.chat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.databinding.ChatDrawerUserItemBinding
import com.mywidget.ui.chat.ChatViewModel

class UserListRecyclerView(val viewModel: ChatViewModel) :
    RecyclerView.Adapter<UserListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val bind = ChatDrawerUserItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.inviteUserList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bindView(viewModel.inviteUserList.value?.get(position) ?: "")
    }
}

class UserListViewHolder(val binding: ChatDrawerUserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(email: String) {
        binding.email = email
    }
}
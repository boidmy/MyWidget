package com.mywidget.ui.friend.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.databinding.FriendListItemBinding
import com.mywidget.ui.friend.FriendViewModel

class FriendRecyclerView(val viewModel: FriendViewModel) : RecyclerView.Adapter<FriendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val bind = FriendListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bindView()
    }
}

class FriendViewHolder(val binding: FriendListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView() {

    }
}
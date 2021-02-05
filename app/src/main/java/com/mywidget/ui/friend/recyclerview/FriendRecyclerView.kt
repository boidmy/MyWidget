package com.mywidget.ui.friend.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.FriendModel
import com.mywidget.databinding.FriendListItemBinding
import com.mywidget.ui.friend.FriendViewModel
import javax.inject.Inject

class FriendRecyclerView @Inject constructor(val viewModel: FriendViewModel) :
    RecyclerView.Adapter<FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val bind = FriendListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.friendList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bindView(viewModel.friendList.value?.get(position), viewModel)
    }
}

class FriendViewHolder(val binding: FriendListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: FriendModel?, viewModel: FriendViewModel) {
        data?.let {
            binding.data = data
            binding.viewModel = viewModel

            binding.favoritesBtn.isSelected = data.favorites
        }

    }
}
package com.mywidget.ui.chatroom.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.WatingRoomItemBinding
import com.mywidget.ui.chatroom.ChatRoomViewModel

class ChatRoomRecyclerView(val viewModel: ChatRoomViewModel) :
    RecyclerView.Adapter<ChatRoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val bind = WatingRoomItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.roomList.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        viewModel.roomList.value?.get(position)?.let { holder.bindView(it, viewModel, position) }
    }
}

class ChatRoomViewHolder(val binding: WatingRoomItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindView(data: RoomDataModel, viewModel: ChatRoomViewModel, position: Int) {
        binding.data = data
        binding.viewModel = viewModel
        binding.position = position
    }
}
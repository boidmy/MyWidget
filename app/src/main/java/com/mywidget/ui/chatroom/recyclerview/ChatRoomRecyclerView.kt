package com.mywidget.ui.chatroom.recyclerview

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.WatingRoomItemBinding
import com.mywidget.ui.chatroom.ChatRoomViewModel
import java.io.Serializable

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
        viewModel.roomList.value?.get(position)?.let { holder.bindView(it, viewModel) }
    }
}

class ChatRoomViewHolder(val binding: WatingRoomItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindView(data: RoomDataModel, viewModel: ChatRoomViewModel) {
        binding.data = data
        binding.viewModel = viewModel

        binding.roomRemove.setOnClickListener {
            viewModel.deleteRoom.value = data
        }
    }
}
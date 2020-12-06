package com.mywidget.ui.chatroom.recyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.WatingRoomItemBinding
import com.mywidget.ui.chatroom.ChatRoomViewModel
import java.io.Serializable

class ChatRoomRecyclerView(val viewModel: ChatRoomViewModel) : RecyclerView.Adapter<ChatRoomRecyclerView.ChatRoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val bind = WatingRoomItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.roomList.value?.size?:0
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        viewModel.roomList.value?.get(position)?.let { holder.bindView(it) }
    }

    inner class ChatRoomViewHolder(val binding: WatingRoomItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: RoomDataModel) {
            binding.data = data

            binding.rvContainer.setOnClickListener {
                val intent = Intent(binding.root.context, ChatActivity::class.java)
                intent.putExtra("roomKey", data.roomKey)
                intent.putExtra("master", data.master)
                intent.putExtra("roomName", data.roomName)
                intent.putExtra("data", data)
                binding.root.context.startActivity(intent)
            }
        }
    }
}
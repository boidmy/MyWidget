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

class ChatRoomRecyclerView(val viewModel: ChatRoomViewModel)
    : RecyclerView.Adapter<ChatRoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val bind = WatingRoomItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return viewModel.roomList.value?.size?:0
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        viewModel.roomList.value?.get(position)?.let { holder.bindView(it, viewModel) }
    }
}

class ChatRoomViewHolder(val binding: WatingRoomItemBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bindView(data: RoomDataModel, viewModel: ChatRoomViewModel) {
        binding.data = data

        binding.rvContainer.setOnClickListener {
            val intent = Intent(binding.root.context, ChatActivity::class.java)
            intent.putExtra("data", data)
            binding.root.context.startActivity(intent)
        }
        val alert = AlertDialog.Builder(binding.root.context)
        binding.roomRemove.setOnClickListener {
            alert
                .setTitle("방에서 나오시겠습니까?")
                .setPositiveButton("나가기") { _, _ ->
                    viewModel.deleteRoom(data.roomKey)
                }
                .setNegativeButton("취소") { _, _ ->
                }.show()
        }
    }
}
package com.mywidget.chat.waiting.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.chat.RoomDataModel
import com.mywidget.chat.waiting.viewmodel.WatingRoomViewModel
import com.mywidget.databinding.WatingRoomItemBinding

class WatingRoomAdapter(val viewModel: WatingRoomViewModel) : RecyclerView.Adapter<WatingRoomAdapter.ChatRoomViewHolder>() {
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
        }
    }
}
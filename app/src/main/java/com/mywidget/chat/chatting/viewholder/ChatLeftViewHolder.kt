package com.mywidget.chat.chatting.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mywidget.chat.ChatDataModel
import com.mywidget.databinding.ChatLeftBinding

class ChatLeftViewHolder(val binding: ChatLeftBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: ChatDataModel) {
        binding.data = data
    }
}
package com.mywidget.ui.chat.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatLeftBinding

class ChatLeftViewHolder(val binding: ChatLeftBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: ChatDataModel) {
        binding.data = data
    }
}
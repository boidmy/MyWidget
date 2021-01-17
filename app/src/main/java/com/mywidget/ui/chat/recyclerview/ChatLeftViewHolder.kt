package com.mywidget.ui.chat.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatLeftBinding
import com.mywidget.ui.chat.ChatViewModel

class ChatLeftViewHolder(val binding: ChatLeftBinding, val viewModel: ChatViewModel)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: ChatDataModel) {
        binding.viewModel = viewModel
        binding.data = data
    }
}
package com.mywidget.ui.chat.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatRightBinding
import com.mywidget.ui.chat.ChatViewModel

class ChatRightViewHolder(val binding: ChatRightBinding, val viewModel: ChatViewModel)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: ChatDataModel) {
        binding.data = data
        binding.viewModel = viewModel
    }
}
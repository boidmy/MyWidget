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

        val timeAr = data.time.split(" ")
        if(timeAr.size > 1) {
            binding.chatTime.text = timeAr[0] + "\n" + timeAr[1] + timeAr[2]
        }
    }
}
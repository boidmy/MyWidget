package com.mywidget.ui.chat.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatRightBinding

class ChatRightViewHolder(val binding: ChatRightBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: ChatDataModel) {
        binding.data = data

        val timeAr = data.time.split(" ")
        if(timeAr.size > 1) {
            binding.chatTime.text = timeAr[0] + "\n" + timeAr[1]
        }
    }
}
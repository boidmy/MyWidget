package com.mywidget.ui.chat.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatLeftBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.chat.ChatViewModel

class ChatLeftViewHolder(parent: ViewGroup, val viewModel: ChatViewModel)
    : ViewHolderBase<ChatLeftBinding>(parent, R.layout.chat_left) {

    fun bindView(data: ChatDataModel) {
        binding.viewModel = viewModel
        binding.data = data
    }
}
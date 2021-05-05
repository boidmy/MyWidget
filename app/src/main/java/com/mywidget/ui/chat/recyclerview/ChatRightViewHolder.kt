package com.mywidget.ui.chat.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.data.model.ChatDataModel
import com.mywidget.databinding.ChatRightBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.chat.ChatViewModel

class ChatRightViewHolder(parent: ViewGroup, val viewModel: ChatViewModel)
    : ViewHolderBase<ChatRightBinding>(parent, R.layout.chat_right) {

    fun bindView(data: ChatDataModel) {
        binding.data = data
        binding.viewModel = viewModel
    }
}
package com.mywidget.chat.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.chat.chatting.viewholder.ChatLeftViewHolder
import com.mywidget.chat.chatting.viewholder.ChatRightViewHolder
import com.mywidget.chat.viewmodel.ChatViewModel
import com.mywidget.databinding.ChatLeftBinding
import com.mywidget.databinding.ChatRightBinding

class ChatAdapter(val viewModel: ChatViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LEFTCHAT = 0
    private val RIGHTCHAT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return if (viewType == RIGHTCHAT) {
            val bind = ChatRightBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ChatRightViewHolder(bind)
        } else {
            val bind = ChatLeftBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ChatLeftViewHolder(bind)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewModel.data.value?.get(position)?.id == viewModel.myId) {
            RIGHTCHAT
        } else {
            LEFTCHAT
        }
    }

    override fun getItemCount(): Int {
        return viewModel.data.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = viewModel.data.value?.get(position)
        data?.let {
            if(holder is ChatRightViewHolder) {
                holder.bindView(it)
            } else if(holder is ChatLeftViewHolder){
                holder.bindView(it)
            }
        }
    }
}
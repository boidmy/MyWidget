package com.mywidget.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHoler>() {

    val LEFTCHAT = 0
    val RIGHTCHAT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHoler {
        val layout: Int =
            if (viewType == LEFTCHAT) R.layout.chat_left
            else R.layout.chat_right

        return ChatViewHoler(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ChatViewHoler, position: Int) {
        holder.bindView()
    }

    inner class ChatViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView() {

        }
    }
}
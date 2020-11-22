package com.mywidget.chat.waiting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R

class WatingRoomAdapter : RecyclerView.Adapter<WatingRoomAdapter.ChatRoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.wating_room_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bindView()
    }

    inner class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {

        }
    }
}
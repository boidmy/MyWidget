package com.mywidget.ui.chatroom.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.model.RoomDataModel
import com.mywidget.databinding.WatingRoomItemBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.chatroom.ChatRoomViewModel

class ChatRoomRecyclerView(val viewModel: ChatRoomViewModel) :
    RecyclerView.Adapter<ChatRoomViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    private fun currentList(): MutableList<DiffUtilDataInterface> = diffUtil.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bindView(currentList()[position] as RoomDataModel, viewModel, position)
    }

    fun setData(data: List<RoomDataModel>) {
        val item: MutableList<RoomDataModel> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilDataInterface>)
    }
}

class ChatRoomViewHolder(parent: ViewGroup) :
    ViewHolderBase<WatingRoomItemBinding>(parent, R.layout.wating_room_item) {

    fun bindView(data: RoomDataModel, viewModel: ChatRoomViewModel, position: Int) {
        binding.data = data
        binding.viewModel = viewModel
        binding.position = position
    }
}
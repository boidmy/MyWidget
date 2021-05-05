package com.mywidget.ui.friend.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.model.FriendModel
import com.mywidget.data.room.Memo
import com.mywidget.databinding.FriendListItemBinding
import com.mywidget.ui.friend.FriendViewModel
import javax.inject.Inject

class FriendAdapter @Inject constructor(val viewModel: FriendViewModel) :
    RecyclerView.Adapter<FriendViewHolder>() {

    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    private fun currentList(): MutableList<DiffUtilDataInterface> = diffUtil.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val bind = FriendListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bindView(currentList()[position] as FriendModel, viewModel)
    }

    fun setData(data: List<FriendModel>) {
        val item: MutableList<FriendModel> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilDataInterface>)
    }
}

class FriendViewHolder(val binding: FriendListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: FriendModel?, viewModel: FriendViewModel) {
        data?.let {
            binding.data = data
            binding.viewModel = viewModel
            binding.favoritesBtn.isSelected = data.favorites
            binding.executePendingBindings()
        }
    }
}
package com.mywidget.ui.main.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentDDayItemBinding
import com.mywidget.ui.main.MainFragmentViewModel

class MainTabMemoAdapter(private val mFragmentViewModel: MainFragmentViewModel) :
    RecyclerView.Adapter<MainTabMemoViewHolder>() {

    var prevClickSeq = Int.MAX_VALUE
    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    private fun currentList(): MutableList<DiffUtilDataInterface> = diffUtil.currentList

    fun detail(seq: Int) {
        prevClickSeq = if (prevClickSeq == seq) {
            Int.MAX_VALUE
        } else {
            seq
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTabMemoViewHolder {
        val bind = MainFragmentDDayItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainTabMemoViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return currentList().size
    }

    override fun onBindViewHolder(holder: MainTabMemoViewHolder, position: Int) {
        holder.bindView(
            currentList()[position] as Memo,
            mFragmentViewModel,
            prevClickSeq
        )
    }

    fun setData(data: List<Memo>) {
        val item: MutableList<Memo> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilDataInterface>)
    }
}

class MainTabMemoViewHolder(val binding: MainFragmentDDayItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(mData: Memo?, mFragmentViewModel: MainFragmentViewModel, seq: Int) {
        binding.apply {
            if (mData == null) return
            data = mData
            viewModel = mFragmentViewModel
            executePendingBindings()
            dDayDetailContainer.isVisible = mData.sequence == seq
        }
    }
}

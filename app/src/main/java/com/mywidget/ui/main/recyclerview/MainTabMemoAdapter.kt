package com.mywidget.ui.main.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentDDayItemBinding
import com.mywidget.ui.main.MainFragmentViewModel

class MainTabMemoAdapter : RecyclerView.Adapter<MainTabMemoViewHolder>() {

    private lateinit var mFragmentViewModel: MainFragmentViewModel
    var prevClickSeq = Int.MAX_VALUE

    fun setViewModel(fragmentViewModel: MainFragmentViewModel) {
        mFragmentViewModel = fragmentViewModel
    }

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
        return mFragmentViewModel.memoData.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MainTabMemoViewHolder, position: Int) {
        holder.bindView(mFragmentViewModel.memoData.value?.get(position), mFragmentViewModel, prevClickSeq)
    }
}

class MainTabMemoViewHolder(val binding: MainFragmentDDayItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

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

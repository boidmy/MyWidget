package com.mywidget.ui.main.recyclerview

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentDDayItemBinding
import com.mywidget.ui.main.MainFragmentViewModel
import util.Util.dpToPx


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

    private val dDayDetailContainer = binding.dDayDetailContainer
    private var saveSeq = -1

    fun bindView(mData: Memo?, mFragmentViewModel: MainFragmentViewModel, seq: Int) {
        binding.apply {
            if (mData == null) return
            data = mData
            viewModel = mFragmentViewModel
            executePendingBindings()

            if (saveSeq == seq) {
                dDayDetailContainer.isVisible = true
            } else {
                if (mData.sequence == seq) {
                    saveSeq = seq
                }
                changeVisibility(mData.sequence == seq)
            }

            memoContainer.setOnClickListener {
                mFragmentViewModel.dDayDetail(mData.sequence?: Int.MAX_VALUE)
            }

            memoRemove.setOnClickListener {
                mFragmentViewModel.deleteDDayDialog.value = mData.sequence
            }
        }
    }

    private fun changeVisibility(isExpanded: Boolean) {
        val height = 140.dpToPx
        val va =
                if (isExpanded) ValueAnimator.ofInt(30.dpToPx, height)
                else ValueAnimator.ofInt(dDayDetailContainer.height, 0)
        va.duration = 600
        va.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            // imageView의 높이 변경
            dDayDetailContainer.layoutParams.height = value
            dDayDetailContainer.requestLayout()
            // imageView가 실제로 사라지게하는 부분
            dDayDetailContainer.isVisible = isExpanded
        }
        va.start()
    }
}

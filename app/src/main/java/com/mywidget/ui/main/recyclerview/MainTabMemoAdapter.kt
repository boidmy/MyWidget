package com.mywidget.ui.main.recyclerview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentDDayItemBinding
import com.mywidget.ui.main.MainFragmentViewModel
import util.Util.dpToPx


class MainTabMemoAdapter : RecyclerView.Adapter<MainTabMemoViewHolder>() {

    lateinit var mFragmentViewModel: MainFragmentViewModel

    fun setViewModel(fragmentViewModel: MainFragmentViewModel) {
        mFragmentViewModel = fragmentViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTabMemoViewHolder {
        val bind = MainFragmentDDayItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainTabMemoViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return mFragmentViewModel.memoData.value?.size?: 0
    }

    override fun onBindViewHolder(holder: MainTabMemoViewHolder, position: Int) {
        holder.bindView(mFragmentViewModel.memoData.value?.get(position), mFragmentViewModel)
    }
}

class MainTabMemoViewHolder(val binding: MainFragmentDDayItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    private val dDayDetailContainer = binding.dDayDetailContainer

    fun bindView(mData: Memo?, mFragmentViewModel: MainFragmentViewModel) {
        binding.apply {
            data = mData
            viewModel = mFragmentViewModel
            executePendingBindings()

            changeVisibility(mFragmentViewModel.dDayDetail.value == mData)

            memoContainer.setOnClickListener {
                mData?.let {
                    if (mFragmentViewModel.dDayDetail.value == it) {
                        mFragmentViewModel.dDayDetail(null)
                    } else {
                        mFragmentViewModel.dDayDetail(it)
                    }
                }
            }

            memoRemove.setOnClickListener {
                mFragmentViewModel.deleteDDayDialog.value = mData?.sequence
            }
        }
    }

    private fun changeVisibility(isExpanded: Boolean) {
        val height = 140.dpToPx
        val va =
            if (isExpanded) ValueAnimator.ofInt(20.dpToPx, height)
            else ValueAnimator.ofInt(height, 0)
        va.duration = 600
        va.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            // imageView의 높이 변경
            dDayDetailContainer.layoutParams.height = value
            dDayDetailContainer.requestLayout()
            // imageView가 실제로 사라지게하는 부분
            dDayDetailContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
        va.start()
    }
}

package com.mywidget.ui.main.recyclerview

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import util.CalendarUtil
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentRvItemBinding
import com.mywidget.ui.main.MainFragmentViewModel


class MainTabMemoAdapter : RecyclerView.Adapter<MainTabMemoAdapter.MainTabMemoViewholder>() {

    private var mFragmentViewModel: MainFragmentViewModel? = null

    fun setViewModel(fragmentViewModel: MainFragmentViewModel?) {
        mFragmentViewModel = fragmentViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTabMemoViewholder {
        val bind = MainFragmentRvItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainTabMemoViewholder(bind)
    }

    override fun getItemCount(): Int {
        return mFragmentViewModel?.memoData?.value?.size?: 0
    }

    override fun onBindViewHolder(holder: MainTabMemoViewholder, position: Int) {
        holder.bindView(mFragmentViewModel?.memoData?.value?.get(position))
    }

    inner class MainTabMemoViewholder(val binding: MainFragmentRvItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindView(mData: Memo?) {
            binding.apply {
                viewModel = mData
                executePendingBindings()
                rvContainer.setOnClickListener {
                    val cal = CalendarUtil.calendar(viewModel?.date)
                    cal?.let {
                        val dpd = DatePickerDialog(root.context
                                , DatePickerDialog.OnDateSetListener { _, _, _, dayOfMonth ->
                        }, CalendarUtil.getYear(cal)
                                , CalendarUtil.getMonth(cal)
                                , CalendarUtil.getNowdate(cal))
                        dpd.show()
                    }
                }
                val alert = AlertDialog.Builder(root.context)
                memoRemove.setOnClickListener {
                    alert
                        .setTitle("삭제할거에염?")
                        .setPositiveButton("삭제") { _, _ ->
                            viewModel?.memo?.let {
                                mFragmentViewModel?.deletMemo(it)
                            }
                        }.setNegativeButton("취소") { _, _ ->
                        }.show()

                }
            }
        }
    }
}

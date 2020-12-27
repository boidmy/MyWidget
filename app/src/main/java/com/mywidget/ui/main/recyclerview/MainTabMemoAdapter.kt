package com.mywidget.ui.main.recyclerview

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentDDayItemBinding
import com.mywidget.ui.main.MainFragmentViewModel
import util.CalendarUtil


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

    fun bindView(mData: Memo?, mFragmentViewModel: MainFragmentViewModel) {
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
                mFragmentViewModel.deleteDDayDialog.value = mData?.sequence
                /*alert
                    .setTitle("삭제하실건가요?")
                    .setPositiveButton("삭제") { _, _ ->
                        mData?.sequence?.let {
                            mFragmentViewModel?.deleteMemo(it)
                        }
                    }.setNegativeButton("취소") { _, _ ->
                    }.show()*/
            }
        }
    }
}

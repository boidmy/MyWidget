package com.mywidget.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.CalendarUtil
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentRvItemBinding
import com.mywidget.viewModel.MainFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainTabMemoAdapter : RecyclerView.Adapter<MainTabMemoAdapter.MainTabMemoViewholder>() {

    private var mFragmentViewModel: MainFragmentViewModel? = null

    fun setViewModel(fragmentViewModel: MainFragmentViewModel?) {
        mFragmentViewModel = fragmentViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTabMemoViewholder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_rv_item, parent, false)
        return MainTabMemoViewholder(view)
    }

    override fun getItemCount(): Int {
        return mFragmentViewModel?.memoData?.value?.size?: 0
    }

    override fun onBindViewHolder(holder: MainTabMemoViewholder, position: Int) {
        holder.bindView(mFragmentViewModel?.memoData?.value?.get(position))
    }

    inner class MainTabMemoViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = MainFragmentRvItemBinding.bind(itemView)
        fun bindView(mData: Memo?) {
            binding.viewModel = mData
            binding.executePendingBindings()
            binding.rvContainer.setOnClickListener {
                val cal = CalendarUtil.calendar(mData?.date)
                cal?.let {
                    val dpd = DatePickerDialog(itemView.context
                            , DatePickerDialog.OnDateSetListener { _, _, _, dayOfMonth ->
                    }, CalendarUtil.getYear(cal)
                            , CalendarUtil.getMonth(cal)
                            , CalendarUtil.getNowdate(cal))
                    dpd.show()
                }

            }


            val alert = AlertDialog.Builder(itemView.context)

            binding.memoRemove.setOnClickListener {
                alert
                    .setTitle("삭제할거에염?")
                    .setPositiveButton("삭제") { _, _ ->
                        mFragmentViewModel?.deletMemo(mData?.memo!!)
                    }.setNegativeButton("취소") { _, _ ->
                    }.show()

            }
        }
    }

    companion object {
        @BindingAdapter("text")
        @JvmStatic
        fun text(textView: TextView?, data: String?) {
            textView?.text = data
        }

        @BindingAdapter("dateProcessing")
        @JvmStatic
        fun dateProcessing(textView: TextView?, data: String?) {
            val cal = CalendarUtil.calendar(data)
            var value = ""
            cal?.let {
                val dayOfWeek: Int = it.get(Calendar.DAY_OF_WEEK)
                value = CalendarUtil.getYear(it).toString()+"-"+String.format("%02d"
                        , CalendarUtil.getMonth(it)+1)+"-"+
                        CalendarUtil.getNowdate(it).toString() +
                        " (" + CalendarUtil.week(dayOfWeek) + ")"
            }

            text(textView, value)
        }

        @BindingAdapter("termProcessing")
        @JvmStatic
        fun termProcessing(textView: TextView?, data: String?) {
            val cal = CalendarUtil.calendar(data)
            var value = ""
            cal?.let {
                value = CalendarUtil.dDay(CalendarUtil.getYear(cal)
                        , CalendarUtil.getMonth(cal)+1
                        , CalendarUtil.getNowdate(cal)).toString()
            }

            text(textView, value)
        }
    }
}

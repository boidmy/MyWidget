package com.mywidget.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.room.Memo
import com.mywidget.viewModel.MainFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainTabRvAdapter : RecyclerView.Adapter<MainTabRvAdapter.MainTabRvViewholder>() {

    private var mData: List<Memo>? = null
    private var mFragmentViewModel: MainFragmentViewModel? = null

    fun setViewModel(fragmentViewModel: MainFragmentViewModel?) {
        mFragmentViewModel = fragmentViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainTabRvViewholder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_fragment_rv_item, parent, false)
        return MainTabRvViewholder(view)
    }

    override fun getItemCount(): Int {
        return mData?.size?: 0
    }

    override fun onBindViewHolder(holder: MainTabRvViewholder, position: Int) {
        holder.bindView(position)
    }

    fun setData(arrayList: List<Memo>?) {
        mData = arrayList
        notifyDataSetChanged()
    }

    inner class MainTabRvViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var memotxt: TextView = itemView.findViewById(R.id.memo_txt)
        private var datetxt: TextView = itemView.findViewById(R.id.date_txt)
        private var memoRemove: ImageView = itemView.findViewById(R.id.memo_remove)
        private var rv_container: ConstraintLayout = itemView.findViewById(R.id.rv_container)
        private var memo_date: TextView = itemView.findViewById(R.id.memo_date)
        fun bindView(position: Int) {
            memotxt.text = mData?.get(position)?.memo

            try {
                val day = mData?.get(position)?.date
                val date = SimpleDateFormat("yyyyMMdd").parse(day)
                val cal: Calendar = Calendar.getInstance()
                cal.time = date

                val year: Int = cal.get(Calendar.YEAR)
                val month: Int = cal.get(Calendar.MONTH) + 1
                val nowdate: Int = cal.get(Calendar.DAY_OF_MONTH)
                val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

                var dateTxt: String = Util.dDay(year, month, nowdate).toString()

                memo_date.text = year.toString()+"-"+String.format("%02d", month)+"-"+nowdate.toString() + " (" + week(dayOfWeek) + ")"
                datetxt.text = dateTxt

                rv_container.setOnClickListener {
                    val dpd = DatePickerDialog(itemView.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    }, year, month-1, nowdate)
                    dpd.show()
                }
            } catch (e:Exception) {

            }

            var alert = AlertDialog.Builder(itemView.context)

            memoRemove.setOnClickListener {
                alert
                    .setTitle("삭제할거에염?")
                    .setPositiveButton("삭제") { _, _ ->
//                        MainApplication.memoDelete(mData?.get(position)?.memo!!)
                        //mCallBack?.notifyCall()
                        Thread(Runnable {
                            mFragmentViewModel?.deletMemo(mData?.get(position)?.memo!!)
                        }).start()
                    }
                    .setNegativeButton("취소") { _, _ ->
                    }
                    .show()

            }
        }

        private fun week(dayOfWeek: Int) : String {
            return when (dayOfWeek) {
                1 -> "일"
                2 -> "월"
                3 -> "화"
                4 -> "수"
                5 -> "목"
                6 -> "금"
                7 -> "토"
                else -> ""
            }
        }
    }
}

package com.mywidget.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.room.Memo
import com.mywidget.databinding.MainFragmentRvItemBinding
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
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_fragment_rv_item, parent, false)
        return MainTabRvViewholder(view)
    }

    override fun getItemCount(): Int {
        return mData?.size?: 0
    }

    override fun onBindViewHolder(holder: MainTabRvViewholder, position: Int) {
        holder.bindView(mData?.get(position))
    }

    fun setData(arrayList: List<Memo>?) {
        mData = arrayList
        notifyDataSetChanged()
    }

    inner class MainTabRvViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = MainFragmentRvItemBinding.bind(itemView)
        fun bindView(mData: Memo?) {
            binding.viewModel = mData
            try {
                val cal: Calendar = Calendar.getInstance()
                cal.time = SimpleDateFormat("yyyyMMdd").parse(mData?.date)
                val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
                val dateTxt: String = Util.dDay(Util.getYear(cal), Util.getMonth(cal)+1
                    , Util.getNowdate(cal)).toString()

                binding.memoDate.text = Util.getYear(cal).toString()+"-"+String.format("%02d"
                    , Util.getMonth(cal)+1)+"-"+ Util.getNowdate(cal).toString() + " (" + Util.week(dayOfWeek) + ")"
                binding.dateTxt.text = dateTxt

                binding.rvContainer.setOnClickListener {
                    val dpd = DatePickerDialog(itemView.context
                        , DatePickerDialog.OnDateSetListener { _, _, _, dayOfMonth ->
                    }, Util.getYear(cal), Util.getMonth(cal), Util.getNowdate(cal))
                    dpd.show()
                }
            } catch (e:Exception) {

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
}

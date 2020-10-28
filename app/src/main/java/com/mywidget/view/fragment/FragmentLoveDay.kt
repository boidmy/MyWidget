package com.mywidget.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.room.LoveDay
import com.mywidget.data.room.LoveDayDB
import com.mywidget.databinding.MainFragmentFragment2Binding
import com.mywidget.viewModel.MainViewModel
import kotlinx.android.synthetic.main.memo_list_dialog.view.*
import kotlinx.android.synthetic.main.memo_list_dialog_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentLoveDay : Fragment() {

    lateinit var binding: MainFragmentFragment2Binding

    override fun onDestroy() {
        super.onDestroy()
        binding.viewModel?.rxClear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.main_fragment_fragment2, container, false)
        bindView()
        selectMemo()
        messagePop()
        return binding.root
    }

    private fun messagePop() {
        binding.viewModel?.message?.observe(this, androidx.lifecycle.Observer {
            val view: View = layoutInflater.inflate(R.layout.memo_list_dialog, null)
            val popupWindow = PopupWindow(
                view,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            for (ds in it) {
                val listItem: View = layoutInflater.inflate(R.layout.memo_list_dialog_item, null)
                listItem.memo.text = ds.memo
                listItem.date.text = ds.date
                view.memo_list_container.addView(listItem)
            }
        })

    }

    private fun bindView() {
        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        binding.lifecycleOwner = this
        binding.viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        binding.viewModel?.loveDayDB = LoveDayDB.getInstance(requireActivity().application)
        Thread(Runnable {
            binding.viewModel?.selectLoveDay()
        }).start()
        binding.viewModel?.loveday?.observe(this, androidx.lifecycle.Observer { data ->
            viewLoveDay(binding, data)
        })
    }

    fun addLoveDay(date: String) {
        Thread(Runnable {
            binding.viewModel?.insertLoveDay(date)
        }).start()
    }

    private fun viewLoveDay(binding: MainFragmentFragment2Binding, data: List<LoveDay>?) {
        try {
            val date = SimpleDateFormat("yyyyMMdd").parse(data?.get(data.size-1)?.date)
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH) + 1
            val nowdate: Int = cal.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
            binding.heartDay.text = Util.loveDay(year, month, nowdate).toString() + "일"
        } catch (e: Exception) {
            binding.heartDay.text = "0일"
        }
    }

    private fun selectMemo() {
        binding.viewModel?.messageLeft("뿡이")
        binding.viewModel?.messageRight("콩이")
        binding.viewModel?.leftMessage?.observe(this, androidx.lifecycle.Observer {
            binding.viewModel?.leftString?.value = it[it.size-1]
        })
        binding.viewModel?.rightMessage?.observe(this, androidx.lifecycle.Observer {
            binding.viewModel?.rightString?.value = it[it.size-1]
        })

    }
}

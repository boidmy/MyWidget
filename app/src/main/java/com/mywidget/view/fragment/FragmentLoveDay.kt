package com.mywidget.view.fragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.LoveDay
import com.mywidget.data.room.LoveDayDB
import com.mywidget.databinding.MainFragmentFragment2Binding
import com.mywidget.viewModel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment_fragment2.view.*
import kotlinx.android.synthetic.main.memo_list_dialog.view.*
import kotlinx.android.synthetic.main.memo_list_dialog_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class FragmentLoveDay : Fragment() {

    private var application: Application? = null
    private var viewModel: MainViewModel? = null
    private var loveDayDB: LoveDayDB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = activity?.application
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.rxClear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: MainFragmentFragment2Binding = DataBindingUtil.inflate(inflater,
            R.layout.main_fragment_fragment2, container, false)
        bindView(binding)
        selectMemo(binding)
        messagePop()
        return binding.root
    }

    private fun messagePop() {
        viewModel?.message?.observe(this, androidx.lifecycle.Observer {
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

    private fun bindView(binding: MainFragmentFragment2Binding) {
        application?.let { it ->
            val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(it)
            binding.lifecycleOwner = this
            viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
            binding.viewModel = viewModel
            loveDayDB = LoveDayDB.getInstance(it)
            viewModel?.loveDayDB = loveDayDB
            Thread(Runnable {
                viewModel?.selectLoveDay()
            }).start()
            viewModel?.loveday?.observe(this, androidx.lifecycle.Observer { data ->
                viewLoveDay(binding, data)
            })
        }
    }

    fun addLoveDay(date: String) {
        Thread(Runnable {
            viewModel?.insertLoveDay(date)
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

    private fun selectMemo(binding: MainFragmentFragment2Binding) {
        viewModel?.leftMessage?.observe(this, androidx.lifecycle.Observer {
            binding.lmemoLeft.text = it[it.size-1].memo
            binding.dateLeft.text = it[it.size-1].date
        })
        viewModel?.rightMessage?.observe(this, androidx.lifecycle.Observer {
            binding.lmemoRight.text = it[it.size-1].memo
            binding.dateRight.text = it[it.size-1].date
        })
        viewModel?.messageData()
    }
}

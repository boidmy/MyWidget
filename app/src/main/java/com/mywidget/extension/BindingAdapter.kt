package com.mywidget.extension

import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import util.CalendarUtil
import com.mywidget.MyAppWidget
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import com.mywidget.ui.widgetlist.recyclerview.WidgetListRecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.ui.chatroom.recyclerview.ChatRoomRecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.data.room.User
import com.mywidget.ui.chat.ChatViewModel
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.widgetlist.WidgetListViewModel
import util.Util
import java.util.*

@BindingAdapter("text")
fun text(textView: TextView?, data: String?) {
    textView?.text = data
}

@BindingAdapter("dateProcessing")
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
fun termProcessing(textView: TextView?, data: String?) {
    val cal = CalendarUtil.calendar(data)
    var value = ""
    cal?.let {
        value = CalendarUtil.dDay(
            CalendarUtil.getYear(cal)
            , CalendarUtil.getMonth(cal)+1
            , CalendarUtil.getNowdate(cal)).toString()
    }

    textView?.let {
        if(value.toInt() >= 0) {
            text(it, "D - $value")
        } else {
                it.textSize = 15f
            text(it, "지난 일정입니다")
        }
    }
}

@BindingAdapter("NowDate")
fun setNowDate(view: View, textView: TextView) {
    view.setOnClickListener {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            textView.text = year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString()
            textView.tag = year.toString()+String.format("%02d", monthOfYear+1)+dayOfMonth.toString()
        }, CalendarUtil.getYear(c), CalendarUtil.getMonth(c), CalendarUtil.getNowdate(c))
        dpd.show()
    }
}

@BindingAdapter("loveDayConfirm", "textTag")
fun setLoveDay(button: Button, viewModel: MainFragmentViewModel, textView: TextView) {
    button.setOnClickListener {
        textView.tag?.let {
            viewModel.addLoveDay(it.toString())
            viewModel.loveDayDialogVisible.value = false
        } ?: run {
            Toast.makeText(button.context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
        }
    }
}

@BindingAdapter("userConfirm", "userPhone", "userviewModel")
fun setUserConfirm(button: Button, name: EditText, phone: EditText, viewModel: WidgetListViewModel) {
    button.setOnClickListener {
        viewModel.insertUser(name.text.toString(), phone.text.toString())
        viewModel.dialogVisible.value = false
    }
}

@BindingAdapter("items")
fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<User>>) {

    val adapter: WidgetListRecyclerView = recyclerView?.adapter as WidgetListRecyclerView
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun memoAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>?) {
    val adapter: MainTabMemoAdapter = recyclerView?.adapter as MainTabMemoAdapter
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun roomAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<RoomDataModel>>) {
    val adapter: ChatRoomRecyclerView = recyclerView?.adapter as ChatRoomRecyclerView
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun chatAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<ChatDataModel>>) {
    val adapter: ChatAdapter = recyclerView?.adapter as ChatAdapter
    adapter.notifyDataSetChanged()
}

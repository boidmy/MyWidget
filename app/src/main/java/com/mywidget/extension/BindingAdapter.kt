package com.mywidget.extension

import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.CalendarUtil
import com.mywidget.MyAppWidget
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.adapter.MainTabMemoAdapter
import com.mywidget.adapter.UserAdapter
import com.mywidget.chat.ChatDataModel
import com.mywidget.chat.RoomDataModel
import com.mywidget.chat.chatting.ChatAdapter
import com.mywidget.chat.waiting.adapter.WatingRoomAdapter
import com.mywidget.data.room.Memo
import com.mywidget.data.room.User
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.main_loveday_dialog.view.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.*
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
        value = CalendarUtil.dDay(CalendarUtil.getYear(cal)
            , CalendarUtil.getMonth(cal)+1
            , CalendarUtil.getNowdate(cal)).toString()
    }

    text(textView, value)
}

@BindingAdapter("memoTxt", "dateTxt", "viewModel")
fun memoOnclick(button: Button, memo: EditText, date: TextView, viewModel: MainFragmentViewModel) {
    button.setOnClickListener {
        AlertDialog.Builder(button.context)
            .setTitle("아싸~")
            .setMessage("♥입력됐대용♥")
            .setIcon(android.R.drawable.ic_menu_save)
            .setPositiveButton("yes") { _, _ ->
                date.tag?.let {
                    viewModel.insertMemo(memo.text.toString(), it.toString())
                    Toast.makeText(button.context, "저장했대요!!", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(button.context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(
                android.R.string.no
            ) { _, _ ->
                // 취소시 처리 로직
                Toast.makeText(button.context, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
            }.show()
        viewModel.dialogVisible.value = false
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
        AlertDialog.Builder(button.context)
            .setTitle("아싸~")
            .setMessage("♥입력됐대용♥")
            .setIcon(android.R.drawable.ic_menu_save)
            .setPositiveButton("yes") { _, _ ->
                // 확인시 처리 로직
                textView.tag?.let {
                    viewModel.addLoveDay(it.toString())
                    Toast.makeText(button.context, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    viewModel.dialogVisible.value = false
                } ?: run {
                    Toast.makeText(button.context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(
                android.R.string.no
            ) { _, _ ->
                // 취소시 처리 로직
                Toast.makeText(button.context, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
                viewModel.dialogVisible.value = false
            }.show()
    }
}

@BindingAdapter("userConfirm", "userPhone", "userviewModel")
fun setUserConfirm(button: Button, name: EditText, phone: EditText, viewModel: UserViewModel) {
    val context = button.context
    button.setOnClickListener {
        AlertDialog.Builder(context)
                .setTitle("아싸~")
                .setMessage("입력됐어요!")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("yes") { _, _ ->
                    viewModel.insertUser(name.text.toString(), phone.text.toString())
                    Toast.makeText(context, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MyAppWidget::class.java)
                    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    context.sendBroadcast(intent)
                    viewModel.dialogVisible.value = false
                }
                .setNegativeButton("no") { _, _ ->
                    // 취소시 처리 로직
                    Toast.makeText(context, "취소했대요", Toast.LENGTH_SHORT).show()
                    viewModel.dialogVisible.value = false
                }.show()
    }

}

@BindingAdapter("items")
fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<User>>) {

    val adapter: UserAdapter = recyclerView?.adapter as UserAdapter
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun memoAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>?) {
    val adapter: MainTabMemoAdapter = recyclerView?.adapter as MainTabMemoAdapter
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun roomAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<RoomDataModel>>) {
    val adapter: WatingRoomAdapter = recyclerView?.adapter as WatingRoomAdapter
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun chatAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<ChatDataModel>>) {
    val adapter: ChatAdapter = recyclerView?.adapter as ChatAdapter
    adapter.notifyDataSetChanged()
}



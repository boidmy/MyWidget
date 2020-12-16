package com.mywidget.extension

import android.app.DatePickerDialog
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import util.CalendarUtil
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import com.mywidget.ui.widgetlist.recyclerview.WidgetListRecyclerView
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.ui.chatroom.recyclerview.ChatRoomRecyclerView
import com.mywidget.data.room.Memo
import com.mywidget.data.room.User
import com.mywidget.ui.chat.recyclerview.UserListRecyclerView
import com.mywidget.ui.chatinvite.recyclerview.ChatInviteRecyclerView
import com.mywidget.ui.friend.recyclerview.FriendRecyclerView
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.widgetlist.WidgetListViewModel
import util.Util.toast
import java.util.*
import kotlin.collections.ArrayList

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
fun setNowDate(calendarTxtArea: EditText, today: String) {
    calendarTxtArea.setText(today)
    calendarTxtArea.setOnClickListener {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(calendarTxtArea.context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendarTxtArea.setText(year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString())
            calendarTxtArea.tag = year.toString()+String.format("%02d", monthOfYear+1)+dayOfMonth.toString()
        }, CalendarUtil.getYear(c), CalendarUtil.getMonth(c), CalendarUtil.getNowdate(c))
        dpd.show()
    }
}

@BindingAdapter("loveDayConfirm", "textTag")
fun setLoveDay(imageView: ImageView, viewModel: MainFragmentViewModel, textView: TextView) {
    imageView.setOnClickListener {
        textView.tag?.let {
            viewModel.addLoveDay(it.toString())
            viewModel.loveDayDialogVisibility(false)
        } ?: run {
            imageView.context.toast("날짜를 선택해주세요!")
        }
    }
}

@BindingAdapter("memoTxt", "dateTxt", "viewModel")
fun memoOnclick(imageView: ImageView, memo: EditText, date: TextView, viewModel: MainFragmentViewModel) {
    imageView.setOnClickListener {
        if (memo.text.toString().isEmpty()) {
            imageView.context.toast("디데이명을 입력해주세요!")
        } else {
            date.tag?.let {
                viewModel.insertMemo(memo.text.toString(), it.toString())
                viewModel.memoDialogVisibility(false)
            } ?: run {
                imageView.context.toast("날짜를 선택해주세요!")
            }
        }
    }
}

@BindingAdapter("favoritesMessage", "viewModel")
fun favoritesMemo(imageView: ImageView, editText: EditText, viewModel: MainFragmentViewModel) {
    imageView.setOnClickListener {
        viewModel.favoritesMessage(editText.text.toString())
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

@BindingAdapter("items")
fun chatUserListAdapter(recyclerView: RecyclerView?, data: MutableLiveData<ArrayList<String>>) {
    val adapter: UserListRecyclerView = recyclerView?.adapter as UserListRecyclerView
    adapter.notifyDataSetChanged()
}

@BindingAdapter("items")
fun friendListAdapter(recyclerView: RecyclerView?, data: MutableLiveData<ArrayList<FriendModel>>) {
    val adapter: FriendRecyclerView = recyclerView?.adapter as FriendRecyclerView
    adapter.notifyDataSetChanged()
}

@BindingAdapter("chatInviteRvAdapter")
fun inviteAdapter(recyclerView: RecyclerView?, data: MutableLiveData<ArrayList<FriendModel>>) {
    val adapter: ChatInviteRecyclerView = recyclerView?.adapter as ChatInviteRecyclerView
    adapter.notifyDataSetChanged()
}
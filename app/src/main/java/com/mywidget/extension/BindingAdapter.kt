package com.mywidget.extension

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.data.model.*
import com.mywidget.data.room.Memo
import com.mywidget.data.room.User
import com.mywidget.ui.chat.ChatViewModel
import com.mywidget.ui.chat.recyclerview.ChatAdapter
import com.mywidget.ui.chat.recyclerview.UserListRecyclerView
import com.mywidget.ui.chatinvite.recyclerview.ChatInviteAdapter
import com.mywidget.ui.chatroom.recyclerview.ChatRoomAdapter
import com.mywidget.ui.friend.recyclerview.FriendAdapter
import com.mywidget.ui.main.MainFragmentViewModel
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import com.mywidget.ui.widgetlist.WidgetListViewModel
import com.mywidget.ui.widgetlist.recyclerview.WidgetListAdapter
import util.CalendarUtil
import util.CalendarUtil.hourDateFormat
import util.CalendarUtil.memoDateFormat
import util.Util.toast
import java.util.*

@BindingAdapter("text")
fun text(textView: TextView?, data: String?) {
    textView?.text = data
}

@BindingAdapter("daysPast")
fun daysPast(textView: TextView, date: Int) {
    textView.apply {
        if (date > 0) {
            textSize = 21f
            text(this, "D - $date")
        } else {
            textSize = 15f
            if (date == 0) {
                text(this, "오늘이에요!")
            } else {
                text(this, "지난 일정이에요!")
            }
        }
    }
}

@BindingAdapter("NowDate")
fun setNowDate(calendarTxtArea: EditText, today: String) {
    //calendarTxtArea.setText(today)
    calendarTxtArea.setOnClickListener {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(calendarTxtArea.context, { view, year, monthOfYear, dayOfMonth ->
            calendarTxtArea.setText(year.toString() + "-" + (monthOfYear + 1).toString() + "-" + dayOfMonth.toString())
            calendarTxtArea.tag =
                year.toString() + String.format("%02d", monthOfYear + 1) + dayOfMonth.toString()
        }, CalendarUtil.getYear(c), CalendarUtil.getMonth(c), CalendarUtil.getNowDate(c))
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
fun memoOnclick(
    imageView: ImageView,
    memo: EditText,
    date: TextView,
    viewModel: MainFragmentViewModel
) {
    imageView.setOnClickListener {
        if (memo.text.toString().isEmpty()) {
            imageView.context.toast("디데이명을 입력해주세요!")
        } else {
            if (date.text.isEmpty()) {
                imageView.context.toast("날짜를 선택해주세요!")
            } else {
                val memoData = Memo(null, memo.text.toString(), date.tag.toString())
                viewModel.insertMemo(memoData)
                viewModel.memoDialogVisibility(false)
            }
        }
    }
}

@BindingAdapter("favoritesMessage", "viewModel")
fun favoritesMemo(imageView: ImageView, editText: EditText, viewModel: MainFragmentViewModel) {
    imageView.setOnClickListener {
        viewModel.favoritesInsertMessage(editText.text.toString())
    }
}

@BindingAdapter("chatNickName", "chatViewModel")
fun chatText(textView: TextView, data: ChatDataModel, viewModel: ChatViewModel) {
    val myInsertNickName = viewModel.friendHashMap[data.id] ?: "" //1순위 내가 등록한 친구 닉네임
    val nickName = viewModel.inviteDatabaseMap[data.id] ?: "" //2순위 상대가 회원가입 할때 등록한 닉네임
    if (myInsertNickName.isNotEmpty()) {
        text(textView, myInsertNickName)
    } else {
        if (nickName.isNotEmpty()) {
            text(textView, nickName)
        } else {
            text(textView, data.id)
        }
    }
}

@BindingAdapter("userConfirm", "userPhone", "userviewModel")
fun setUserConfirm(
    button: Button,
    name: EditText,
    phone: EditText,
    viewModel: WidgetListViewModel
) {
    button.setOnClickListener {
        viewModel.insertUser(name.text.toString(), phone.text.toString())
        viewModel.dialogVisible.value = false
    }
}

@BindingAdapter("chatEditListener")
fun chatEditListener(editText: EditText, view: Button) {
    val drawable = ResourcesCompat
        .getDrawable(
            editText.resources,
            R.drawable.edge_round_smal_corner,
            null
        ) as GradientDrawable
    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0?.length ?: 0 > 0) {
                drawable.setColor(Color.parseColor("#45D4FA"))
                view.background = drawable
                view.isEnabled = true
            } else {
                drawable.setColor(Color.parseColor("#C6C6C6"))
                view.background = drawable
                view.isEnabled = false
            }
        }
    })
}

@BindingAdapter("favoriteFriendVisibility")
fun favoriteFriendVisibility(view: View, data: UserData?) {
    data?.let {
        if (it.email.isNotEmpty()) {
            view.isVisible = true
            return
        }
    }
    view.isVisible = false
}

@BindingAdapter("roomLastMessage", "position", "time")
fun roomLastMessage(textView: TextView, data: List<ChatDataModel>?, position: Int, time: TextView) {
    data?.let {
        if (it.size > position) {
            text(textView, it[position].message)
            text(time, hourDateFormat(it[position].time))
        }
    }
}

@BindingAdapter("items")
fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<User>>) {
    val adapter: WidgetListAdapter = recyclerView?.adapter as WidgetListAdapter
    adapter.setData(data.value ?: listOf())
}

@BindingAdapter("items")
fun memoAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<Memo>>) {
    val adapter: MainTabMemoAdapter = recyclerView?.adapter as MainTabMemoAdapter
    adapter.setData(data.value ?: listOf())
}

@BindingAdapter("items")
fun roomAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<RoomDataModel>>) {
    val adapter: ChatRoomAdapter = recyclerView?.adapter as ChatRoomAdapter
    adapter.setData(data.value ?: listOf())
}

@BindingAdapter("items")
fun chatAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<ChatData>>) {
    val adapter: ChatAdapter = recyclerView?.adapter as ChatAdapter
    adapter.setData(data.value ?: listOf())
    recyclerView.smoothScrollToPosition(0)
}

@BindingAdapter("items")
fun chatUserListAdapter(
    recyclerView: RecyclerView?,
    data: MutableLiveData<List<ChatInviteModel>>
) {
    val adapter: UserListRecyclerView = recyclerView?.adapter as UserListRecyclerView
    adapter.setData(data.value ?: listOf())
}

@BindingAdapter("items")
fun friendListAdapter(
    recyclerView: RecyclerView?,
    data: MutableLiveData<List<FriendModel>>
) {
    val adapter: FriendAdapter = recyclerView?.adapter as FriendAdapter
    adapter.setData(data.value ?: listOf())
}

@BindingAdapter("chatInviteRvAdapter")
fun inviteAdapter(recyclerView: RecyclerView?, data: MutableLiveData<List<FriendModel>>) {
    val adapter: ChatInviteAdapter = recyclerView?.adapter as ChatInviteAdapter
    adapter.setData(data.value ?: listOf())
}
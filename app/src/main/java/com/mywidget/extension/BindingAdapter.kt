package com.mywidget.extension

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.mywidget.CalendarUtil
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.viewModel.MainFragmentViewModel
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
                viewModel.insertMemo(memo.text.toString(), date.tag.toString())
                Toast.makeText(button.context, "저장했대요!!", Toast.LENGTH_SHORT).show()
                viewModel.visible.value = false
                viewModel.memovisible.value = false
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }
            .setNegativeButton(
                android.R.string.no
            ) { _, _ ->
                // 취소시 처리 로직
                Toast.makeText(button.context, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
                viewModel.memovisible.value = false
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            }
            .show()
    }
}

/*
@BindingAdapter("onClickDate")
fun onClickDate(constraintLayout: ConstraintLayout, data: String?) {
    val cal = CalendarUtil.calendar(data)
    val dpd = DatePickerDialog(constraintLayout.context
            , DatePickerDialog.OnDateSetListener { _, _, _, dayOfMonth ->
    }, CalendarUtil.getYear(cal)
            , CalendarUtil.getMonth(cal)
            , CalendarUtil.getNowdate(cal))
    dpd.show()
}*/

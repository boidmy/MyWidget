package com.mywidget.extension

import android.app.DatePickerDialog
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.mywidget.CalendarUtil
import com.mywidget.Util
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

package com.mywidget.extension

import android.app.DatePickerDialog
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.mywidget.CalendarUtil
import com.mywidget.Util
import java.util.*



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

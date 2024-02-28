package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mywidget.data.Interface.DiffUtilDataInterface
import util.CalendarUtil

@Entity(tableName = "memo")
class Memo(
    @PrimaryKey(autoGenerate = true) var sequence: Int?,
    @ColumnInfo(name = "memo") var memo: String?,
    @ColumnInfo(name = "date") var date: String?
) : DiffUtilDataInterface {
    override fun keyValue(): String {
        return sequence.toString()
    }

    override fun contentValue(): String {
        return memo ?: ""
    }

    fun copy(sequence: Int?, memo: String?, date: String?): Memo {
        return Memo(sequence, memo, date)
    }

    fun daysPast(): Int {
        val cal = CalendarUtil.calendar(date)
        var value = ""
        cal?.let {
            value = CalendarUtil.dDay(
                CalendarUtil.getYear(cal)
                , CalendarUtil.getMonth(cal) + 1
                , CalendarUtil.getNowDate(cal)
            ).toString()
        }

        return value.toInt()
    }

    fun daysFullFormat(): String {
        val cal = CalendarUtil.calendar(date)
        return cal?.let {
            CalendarUtil.memoDateFormat(it)
        } ?: {
            ""
        }()
    }
}
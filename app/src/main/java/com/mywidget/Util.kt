package com.mywidget

import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun howMuchloveDay(value: String?): String {
        value?.let {
            val simpleData = SimpleDateFormat("yyyyMMdd").parse(it)
            val calender: Calendar = Calendar.getInstance()
            calender.time = simpleData
            val year: Int = calender.get(Calendar.YEAR)
            val month: Int = calender.get(Calendar.MONTH) + 1
            val date: Int = calender.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek: Int = calender.get(Calendar.DAY_OF_WEEK)

            val today = GregorianCalendar()
            val nYear1 = today[Calendar.YEAR]
            val nMonth1 = today[Calendar.MONTH] + 1
            val nDate1 = today[Calendar.DATE]
            val cal = Calendar.getInstance()
            var nTotalDate1 = 0
            var nTotalDate2 = 0
            var nDiffOfYear = 0
            var nDiffOfDay = 0
            if (nYear1 > year) {
                for (i in year until nYear1) {
                    cal[i, 12] = 0
                    nDiffOfYear += cal[Calendar.DAY_OF_YEAR]
                }
                nTotalDate1 += nDiffOfYear
            } else if (nYear1 < year) {
                for (i in nYear1 until year) {
                    cal[i, 12] = 0
                    nDiffOfYear += cal[Calendar.DAY_OF_YEAR]
                }
                nTotalDate2 += nDiffOfYear
            }
            cal[nYear1, nMonth1 - 1] = nDate1
            nDiffOfDay = cal[Calendar.DAY_OF_YEAR]
            nTotalDate1 += nDiffOfDay
            cal[year, month - 1] = date
            nDiffOfDay = cal[Calendar.DAY_OF_YEAR]
            nTotalDate2 += nDiffOfDay
            return (nTotalDate1 - nTotalDate2 + 1).toString()+"일"
        } ?: run {
            return "0"
        }

    }

    fun dDay(year: Int, month: Int, day: Int): Int {
        val today = GregorianCalendar()
        val nYear2 = today[Calendar.YEAR]
        val nMonth2 = today[Calendar.MONTH] + 1
        val nDate2 = today[Calendar.DATE]
        val cal = Calendar.getInstance()
        var nTotalDate1 = 0
        var nTotalDate2 = 0
        var nDiffOfYear = 0
        var nDiffOfDay = 0
        if (year > nYear2) {
            for (i in nYear2 until year) {
                cal[i, 12] = 0
                nDiffOfYear += cal[Calendar.DAY_OF_YEAR]
            }
            nTotalDate1 += nDiffOfYear
        } else if (year < nYear2) {
            for (i in year until nYear2) {
                cal[i, 12] = 0
                nDiffOfYear += cal[Calendar.DAY_OF_YEAR]
            }
            nTotalDate2 += nDiffOfYear
        }
        cal[year, month - 1] = day
        nDiffOfDay = cal[Calendar.DAY_OF_YEAR]
        nTotalDate1 += nDiffOfDay
        cal[nYear2, nMonth2 - 1] = nDate2
        nDiffOfDay = cal[Calendar.DAY_OF_YEAR]
        nTotalDate2 += nDiffOfDay
        return nTotalDate1 - nTotalDate2
    }
}
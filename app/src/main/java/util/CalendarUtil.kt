package util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    fun calendar(data: String?): Calendar? {
        data?.let {

        }
        if(data != "null") {
            val cal: Calendar = Calendar.getInstance()
            cal.time = SimpleDateFormat("yyyyMMdd").parse(data)
            return cal
        }
        return null
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

    fun getYear(cal: Calendar): Int {
        return cal.get(Calendar.YEAR)
    }

    fun getMonth(cal: Calendar): Int {
        return cal.get(Calendar.MONTH)
    }

    fun getNowdate(cal: Calendar): Int {
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    fun week(dayOfWeek: Int) : String {
        return when (dayOfWeek) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }
    }

}
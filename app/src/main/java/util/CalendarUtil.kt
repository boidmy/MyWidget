package util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {

    fun calendar(data: String?): Calendar? {
        if(data != "null" && data != null) {
            val cal: Calendar = Calendar.getInstance(Locale.KOREA)
            cal.time = SimpleDateFormat("yyyyMMdd").parse(data)
            return cal
        }
        return null
    }

    fun dDay(year: Int, month: Int, day: Int): Int {
        val today = GregorianCalendar(Locale.KOREA)
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

    fun getNowDate(cal: Calendar): Int {
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun week(dayOfWeek: Int) : String {
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

    fun howMuchLoveDay(value: String?): String {
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

    fun getToday(): String {
        val cal = Calendar.getInstance()
        return "${getYear(cal)}-${getMonth(cal)+1}-${getNowDate(cal)}"
    }

    fun getDate(): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
        val calendar = Calendar.getInstance()
        val date = calendar.time
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        return dateFormat.format(date)
    }

    private fun defaultDataFormat(data: String): Date? {
        return if (data.isNotEmpty()) {
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
            format.parse(data)
        } else {
            null
        }
    }

    fun yearDateFormat(data: String): String {
        with(defaultDataFormat(data)) {
            return if (this != null) {
                val outputFormat = SimpleDateFormat("yy.MM.dd a hh:mm", Locale.getDefault())
                outputFormat.format(this)
            } else {
                ""
            }
        }
    }

    fun hourDateFormat(data: String): String {
        with(defaultDataFormat(data)) {
            return if (this != null) {
                val outputFormat = SimpleDateFormat("a hh:mm", Locale.getDefault())
                outputFormat.format(this)
            } else {
                ""
            }
        }
    }

    fun memoDateFormat(cal: Calendar): String {
        val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
        return getYear(cal).toString()+"-"+ String.format("%02d", getMonth(cal)+1)+"-"+
                String.format("%02d", getNowDate(cal)) + " (" + week(dayOfWeek) + ")"
    }
}
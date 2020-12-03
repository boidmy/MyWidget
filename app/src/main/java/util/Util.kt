package util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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

    fun upKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun downKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun userIdFormat(userEmail: String): String {
        val mEmail: List<String>? = userEmail.split("@")
        var userId = ""
        if (mEmail?.size ?: 0 > 0) {
            userId = mEmail?.get(0) ?: ""
        }
        return userId
    }

    fun toast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun replacePointToComma(email: String): String {
        return email.replace(".", ",")
    }
}
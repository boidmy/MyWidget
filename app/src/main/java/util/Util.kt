package util

import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Util {

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
//
    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun replacePointToComma(email: String): String {
        return email.replace(".", ",").trim()
    }

    fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
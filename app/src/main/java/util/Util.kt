package util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Util {

    fun upKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun downKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    fun replacePointToComma(email: String): String {
        return email.replace(".", ",").trim()
    }

    fun replaceCommaToPoint(email: String): String {
        return email.replace(",", ".").trim()
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    val Int.dpToPx: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
}
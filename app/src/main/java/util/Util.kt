package util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.mywidget.R
import kotlin.math.roundToLong

object Util {

    fun replacePointToComma(email: String) = email.replace(".", ",").trim()

    fun replaceCommaToPoint(email: String) = email.replace(",", ".").trim()

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).roundToLong().toInt()

    fun View.click(block: (View) -> Unit) = setOnClickListener(block)

    infix fun Context.firebaseAuthException(exception: String) {
        when (exception) {
            "ERROR_INVALID_EMAIL" -> toast(getString(R.string.emailFormatNotCorrect))
            "ERROR_USER_NOT_FOUND" -> toast(getString(R.string.emailAddressCheck))
            "ERROR_WEAK_PASSWORD" -> toast(getString(R.string.passwordCount))
            else -> toast(getString(R.string.error))
        }
    }

    fun upKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun downKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
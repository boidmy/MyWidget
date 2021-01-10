package util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlin.math.roundToLong

object Util {

    fun upKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun downKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        get() = (this * Resources.getSystem().displayMetrics.density).roundToLong().toInt()

    fun firebaseAuthException(exception: String, context: Context) {
        when (exception) {
            "ERROR_INVALID_EMAIL" -> context.toast("이메일 형식이 올바르지 않습니다.")
            "ERROR_USER_NOT_FOUND" -> context.toast("이메일 주소를 다시 확인해 주세요.")
            "ERROR_WEAK_PASSWORD" -> context.toast("비밀번호는 6자 이상으로 입력해 주세요.")
            else -> context.toast("에러가 발생했습니다. 잠시후 다시 시도해 주세요.")
        }
    }
}
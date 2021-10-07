package util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.mywidget.data.BUNDLE
import com.mywidget.data.Constants
import com.mywidget.data.Constants.Companion.REQUEST_CODE_FLOATING
import com.mywidget.data.Landing
import com.mywidget.data.RouterEvent
import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chatinvite.ChatInviteActivity
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.ui.friend.FriendActivity
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.login.signup.SignUpActivity
import com.mywidget.ui.loveday.FloatingPopupActivity
import com.mywidget.ui.mypage.MyPageActivity
import com.mywidget.ui.widgetlist.WidgetListActivity

object LandingRouter {
    fun Context.move(event: RouterEvent) {
        when (event.type) {
            Landing.WIDGET -> goWidget()
            Landing.FLOATING -> goFloating()
            Landing.CHAT_ROOM -> goChatRoom()
            Landing.CHAT -> goChat(event)
            Landing.CHAT_INVITE -> goChatInvite(event)
            Landing.LOGIN -> goLogin()
            Landing.SIGN_UP -> goSignUp()
            Landing.FRIEND -> goFriendAdd()
            Landing.MYPAGE -> goMyPage()
        }
    }

    private fun Context.goWidget() {
        val intent = Intent(this, WidgetListActivity::class.java)
        startActivity(intent)
    }

    private fun Context.goFloating() {
        val intent = Intent(this, FloatingPopupActivity::class.java)
        startActivityForResult(this as Activity, intent, REQUEST_CODE_FLOATING, null)
    }

    private fun Context.goChatRoom() {
        val intent = Intent(this, ChatRoomActivity::class.java)
        startActivity(intent)
    }

    private fun Context.goChat(event: RouterEvent) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(BUNDLE, event.data)
        startActivity(intent)
    }

    private fun Context.goChatInvite(event: RouterEvent) {
        val intent = Intent(this, ChatInviteActivity::class.java)
        intent.putExtra(BUNDLE, event.data)
        startActivity(intent)
    }

    private fun Context.goLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(this as Activity, intent, Constants.REQUEST_CODE_LOGIN, null)
    }

    private fun Context.goSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivityForResult(this as Activity, intent, Constants.REQUEST_PASSWORD_SIGN_IN, null)
    }

    private fun Context.goFriendAdd() {
        val intent = Intent(this, FriendActivity::class.java)
        startActivity(intent)
    }

    private fun Context.goMyPage() {
        val intent = Intent(this, MyPageActivity::class.java)
        startActivity(intent)
    }
}
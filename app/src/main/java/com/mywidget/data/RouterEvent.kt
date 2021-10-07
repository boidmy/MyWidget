package com.mywidget.data

import android.os.Bundle


const val INTENT_EXTRA_DATA = "data"
const val INTENT_EXTRA_FRIEND_DATA = "friendMap"
const val BUNDLE = "bundle"
const val RESULT = "result"
const val MEMO = "memo"
const val CONDITION = "condition"
const val D_DAY = "dDay"
const val CHAT = "chat"

class RouterEvent(
    val type: Landing,
    var data: Bundle? = null
)

enum class Landing {
    MAIN,
    WIDGET,
    FLOATING,
    CHAT_ROOM,
    CHAT,
    CHAT_INVITE,
    LOGIN,
    SIGN_UP,
    FRIEND,
    MYPAGE
}
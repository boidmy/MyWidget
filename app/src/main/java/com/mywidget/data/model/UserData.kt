package com.mywidget.data.model


data class UserData(var email: String = "",
                    var friendName: String = "",
                    var uid: String = "",
                    var nickName: String = "") {

    fun getFriendNickName(): String {
        return if (nickName.isEmpty()) {
            "친구"
        } else {
            nickName
        }
    }
}


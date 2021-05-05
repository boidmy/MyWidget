package com.mywidget.data.model

import com.mywidget.data.Interface.DiffUtilDataInterface

data class ChatInviteModel(
    var nickName: String = "",
    var inviteFlag: Boolean = false, //초대된 유저는 true
    var email: String = ""
): DiffUtilDataInterface {
    override fun keyValue(): String {
        return email
    }

    override fun contentValue(): String {
        return nickName
    }

}
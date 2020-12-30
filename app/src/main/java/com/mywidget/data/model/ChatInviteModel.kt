package com.mywidget.data.model

data class ChatInviteModel(
    var nickName: String = "",
    var inviteFlag: Boolean = false, //초대된 유저는 true
    var email: String = ""
)
package com.mywidget.data.model

import java.io.Serializable

data class FriendModel(
    var email: String = "",
    var nickName: String = "",
    var selector: Boolean = false,
    var favorites: Boolean = false
): Serializable
package com.mywidget.data.model

data class FriendModel(
    var email: String,
    var explanation: String,
    var selector: Boolean = false,
    var favorites: Boolean = false
)
package com.mywidget.data.model

data class ChatDataModel (
    var message: String = "",
    var id: String = "",
    var time: String = "",
    var nickName: String = ""
) {
    fun timeVal(): String {
        val timeAr = time.split(" ")
        return if(timeAr.size > 2) {
            timeAr[0] + "\n" + timeAr[1] + timeAr[2]
        } else {
            ""
        }
    }
}
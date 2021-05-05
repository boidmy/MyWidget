package com.mywidget.data.model

import com.mywidget.data.Interface.DiffUtilDataInterface

data class ChatData(
    var key: String = "",
    var chatDataModel: ChatDataModel = ChatDataModel()
): DiffUtilDataInterface {
    override fun keyValue(): String {
        return key
    }

    override fun contentValue(): String {
        return chatDataModel.message
    }

}

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
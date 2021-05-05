package com.mywidget.data.model

import com.mywidget.data.Interface.DiffUtilDataInterface
import java.io.Serializable

data class RoomDataModel (
    var roomName: String = "",
    var roomKey: String = "",
    var master: String = "",
    var lastMessage: String = ""
): Serializable, DiffUtilDataInterface {
    override fun keyValue(): String {
        return roomKey
    }

    override fun contentValue(): String {
        return roomName
    }
}
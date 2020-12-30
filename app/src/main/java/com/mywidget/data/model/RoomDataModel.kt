package com.mywidget.data.model

import java.io.Serializable

data class RoomDataModel (
    var roomName: String = "",
    var roomKey: String = "",
    var master: String = ""
): Serializable
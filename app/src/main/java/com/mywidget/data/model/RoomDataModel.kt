package com.mywidget.data.model

import java.io.Serializable

data class RoomDataModel (
    var roomName: String,
    var key: String,
    var master: String
): Serializable
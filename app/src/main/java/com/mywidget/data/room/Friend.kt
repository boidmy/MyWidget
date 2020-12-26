package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friendList")
class Friend(@PrimaryKey(autoGenerate = true) var sequence: Int,
            @ColumnInfo(name = "email") var email: String,
            @ColumnInfo(name = "nickName") var nickName: String)
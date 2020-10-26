package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(@PrimaryKey(autoGenerate = true) var sequence: Int?,
           @ColumnInfo(name = "name") var name: String?,
           @ColumnInfo(name = "phone") var number: String?) {
    //constructor(): this()
}
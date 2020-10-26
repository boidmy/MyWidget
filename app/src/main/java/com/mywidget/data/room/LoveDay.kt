package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loveDay")
class LoveDay(@PrimaryKey(autoGenerate = true) var sequence: Int?,
              @ColumnInfo(name = "date") var date: String?){
}
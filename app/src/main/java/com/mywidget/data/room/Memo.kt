package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
class Memo(@PrimaryKey(autoGenerate = true) var sequence: Int?,
            @ColumnInfo(name = "memo") var memo: String?,
            @ColumnInfo(name = "date") var date: String?,
            @ColumnInfo(name = "isSelected") var isSelected: Boolean = false)
package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mywidget.data.Interface.DiffUtilDataInterface

@Entity(tableName = "memo")
class Memo(@PrimaryKey(autoGenerate = true) var sequence: Int?,
            @ColumnInfo(name = "memo") var memo: String?,
            @ColumnInfo(name = "date") var date: String?): DiffUtilDataInterface {
    override fun keyValue(): String {
        return sequence.toString()
    }

    override fun contentValue(): String {
        return memo ?: ""
    }
}
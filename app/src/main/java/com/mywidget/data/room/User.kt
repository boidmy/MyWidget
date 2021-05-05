package com.mywidget.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mywidget.data.Interface.DiffUtilDataInterface

@Entity(tableName = "user")
class User(@PrimaryKey(autoGenerate = true) var sequence: Int?,
           @ColumnInfo(name = "name") var name: String?,
           @ColumnInfo(name = "phone") var number: String?): DiffUtilDataInterface {
    override fun keyValue(): String {
        return sequence.toString()
    }

    override fun contentValue(): String {
        return number.toString()
    }
}
package com.mywidget.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mywidget.data.room.Memo

@Dao
abstract class MemoDao : BaseDao<Memo>{
    @Query("SELECT * FROM memo")
    abstract fun getUser(): List<Memo>
}
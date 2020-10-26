package com.mywidget.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mywidget.data.room.Memo

@Dao
interface MemoDao {
    @Query("SELECT * FROM memo")
    fun getUser(): List<Memo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: Memo)

    @Query("DELETE from memo where memo = :memo")
    fun delete(memo: String)
}
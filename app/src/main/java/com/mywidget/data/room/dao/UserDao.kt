package com.mywidget.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.mywidget.data.room.User

@Dao
abstract class UserDao : BaseDao<User>{
    @Query("SELECT * FROM user")
    abstract fun getUser(): List<User>

    @Query("DELETE from user where sequence = :seq")
    abstract fun delete(seq: Int)
}
package com.mywidget.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mywidget.data.room.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): List<User>

    @Insert(onConflict = REPLACE)
    fun insert(user: User)

    @Query("DELETE from user where name = :name")
    fun delete(name: String)
}
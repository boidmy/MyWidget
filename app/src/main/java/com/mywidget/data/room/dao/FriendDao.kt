package com.mywidget.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.mywidget.data.room.Friend

@Dao
abstract class FriendDao : BaseDao<Friend> {
    @Query("SELECT * FROM friendList")
    abstract fun getFriendList(): List<Friend>
}
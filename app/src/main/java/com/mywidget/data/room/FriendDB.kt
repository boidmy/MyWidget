package com.mywidget.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mywidget.data.room.dao.FriendDao

@Database(entities = [Friend::class], version = 1)
abstract class FriendDB: RoomDatabase() {
    abstract fun FriendDao(): FriendDao
}
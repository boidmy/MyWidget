package com.mywidget.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mywidget.data.room.dao.MemoDao

@Database(entities = [Memo::class], version = 1)
abstract class MemoDB: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}
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

    companion object {
        private var INSTANCE: MemoDB?= null

        /*fun getInstance(context: Context): MemoDB? {
            if(INSTANCE == null) {
                synchronized(MemoDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            MemoDB::class.java, "memo.db")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }*/
        fun getInstance(context: Context): MemoDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }
        }
        private fun buildDatabase(context: Context): MemoDB {
            return Room.databaseBuilder(context.applicationContext, MemoDB::class.java, "memo.db")
                .addCallback(object : RoomDatabase.Callback() {
                }).build()
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}
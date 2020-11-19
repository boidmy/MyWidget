package com.mywidget.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mywidget.data.room.dao.LoveDayDao

@Database(entities = [LoveDay::class], version = 1)
abstract class LoveDayDB : RoomDatabase() {
    abstract fun loveDayDao(): LoveDayDao
}
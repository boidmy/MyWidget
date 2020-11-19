package com.mywidget.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mywidget.data.room.LoveDayDB
import com.mywidget.data.room.MemoDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideLoveday(application: Application) : LoveDayDB {
        return Room.databaseBuilder(application, LoveDayDB::class.java, "loveday.db")
            .addCallback(object : RoomDatabase.Callback() {}).build()
    }

    @Provides
    @Singleton
    fun provideMemo(application: Application) : MemoDB {
        return Room.databaseBuilder(application, MemoDB::class.java, "memo.db")
            .addCallback(object : RoomDatabase.Callback() {}).build()
    }
}
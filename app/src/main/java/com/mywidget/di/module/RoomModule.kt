package com.mywidget.di.module

import android.app.Application
import com.mywidget.data.room.MemoDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideMemo(application: Application) : MemoDB {
        return MemoDB.getInstance(application)
    }
}
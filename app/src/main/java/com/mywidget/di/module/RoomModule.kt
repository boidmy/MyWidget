package com.mywidget.di.module

import android.app.Application
import com.mywidget.data.room.MemoDB
import dagger.Module
import dagger.Provides

@Module
class RoomModule {

    @Provides
    fun provideMemo(application: Application) : MemoDB {
        return MemoDB.getInstance(application)
    }
}
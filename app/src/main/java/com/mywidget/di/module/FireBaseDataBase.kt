package com.mywidget.di.module

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
class FireBaseDataBase {

    @Provides
    fun provideDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}
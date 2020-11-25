package com.mywidget.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FireBaseDataBaseModule {

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun provideDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference
    }

    @Provides
    @Named("Room")
    fun provideDatabase(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child("Room")
    }
}
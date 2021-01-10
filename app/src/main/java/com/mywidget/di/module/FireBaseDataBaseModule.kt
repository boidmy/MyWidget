package com.mywidget.di.module

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class FireBaseDataBaseModule {

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference
    }

    @Provides
    @Named("Room")
    @Singleton
    fun provideDatabase(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child("Room")
    }

    @Provides
    @Named("User")
    @Singleton
    fun provideUserDatabase(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child("User")
    }

    @Provides
    @Named("favorites")
    @Singleton
    fun provideFavoritesDatabase(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.reference.child("favorites")
    }
}
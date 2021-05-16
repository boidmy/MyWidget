package com.mywidget

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mywidget.di.compoenet.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MainApplication : DaggerApplication() {

    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    init {
        INSTANSE = this
    }

    companion object {
        lateinit var INSTANSE: MainApplication
    }

    fun authUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
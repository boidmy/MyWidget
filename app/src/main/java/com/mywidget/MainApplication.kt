package com.mywidget

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.mywidget.di.compoenet.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MainApplication : DaggerApplication() {

    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    init {
        INSTANSE = this
    }

    companion object {

        lateinit var INSTANSE: MainApplication
    }

    fun loginEmail(): String {
        return firebaseAuth.currentUser?.email?:""
    }
}
package com.mywidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.RemoteViews
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import com.mywidget.di.compoenet.ApplicationComponent
import com.mywidget.di.compoenet.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class MainApplication : DaggerApplication() {

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

}
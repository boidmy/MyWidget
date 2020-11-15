package com.mywidget

import android.app.Application
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

class MainApplication : Application() {

    var editor: SharedPreferences.Editor? = null
    var mSharedPreference: SharedPreferences? = null
    var googleIdtoken: String? = null
    var userDb: UserDB? = null
    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.create()
        component.inject(this)

        userDb = UserDB.getInstance(INSTANSE)
        mSharedPreference = getSharedPreferences("unme", Context.MODE_PRIVATE)
        editor = mSharedPreference?.edit()
    }

    init {
        INSTANSE = this
    }

    fun getApplicationCompoenet() : ApplicationComponent {
        return component
    }
    companion object {

        lateinit var INSTANSE: MainApplication

        fun widgetBroad() {
            var userList: List<User>

            Thread(Runnable {
                userList = INSTANSE.userDb?.userDao()?.getUser()!!
                val appWidgetManager = AppWidgetManager.getInstance(INSTANSE)
                val thisWidget = ComponentName(INSTANSE, MyAppWidget::class.java)
                val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                for(widgetId in allWidgetIds) {
                    val remoteViews = RemoteViews(INSTANSE.packageName, R.layout.my_app_widget)

                    INSTANSE.editor = INSTANSE.mSharedPreference?.edit()

                    remoteViews.setViewVisibility(R.id.widgetitemcontainer1, View.GONE)
                    remoteViews.setViewVisibility(R.id.widgetitemcontainer2, View.GONE)
                    remoteViews.setViewVisibility(R.id.widgetitemcontainer3, View.GONE)
                    remoteViews.setViewVisibility(R.id.widgetitemcontainer4, View.GONE)
                    remoteViews.setViewVisibility(R.id.widgetitemcontainer5, View.GONE)
                    remoteViews.setViewVisibility(R.id.itemcontainer, View.GONE)

                    if(userList.isEmpty()) {
                        INSTANSE.editor?.remove("number1")?.remove("number2")?.remove("number3")?.remove("number4")?.remove("number5")
                        INSTANSE.editor?.apply()
                    }

                    for(i in userList.indices) {
                        remoteViews.setViewVisibility(R.id.itemcontainer, View.VISIBLE)
                        if(i == 0) {
                            remoteViews.setTextViewText(R.id.name1, userList[i].name)
                            remoteViews.setViewVisibility(R.id.widgetitemcontainer1, View.VISIBLE)
                            INSTANSE.editor?.putString("number1", userList[i].number)
                        }
                        if(i == 1) {
                            remoteViews.setTextViewText(R.id.name2, userList[i].name)
                            remoteViews.setViewVisibility(R.id.widgetitemcontainer2, View.VISIBLE)
                            INSTANSE.editor?.putString("number2", userList[i].number)
                        }
                        if(i == 2) {
                            remoteViews.setTextViewText(R.id.name3, userList[i].name)
                            remoteViews.setViewVisibility(R.id.widgetitemcontainer3, View.VISIBLE)
                            INSTANSE.editor?.putString("number3", userList[i].number)
                        }
                        if(i == 3) {
                            remoteViews.setTextViewText(R.id.name4, userList[i].name)
                            remoteViews.setViewVisibility(R.id.widgetitemcontainer4, View.VISIBLE)
                            INSTANSE.editor?.putString("number4", userList[i].number)
                        }
                        if(i == 4) {
                            remoteViews.setTextViewText(R.id.name5, userList[i].name)
                            remoteViews.setViewVisibility(R.id.widgetitemcontainer5, View.VISIBLE)
                            INSTANSE.editor?.putString("number5", userList[i].number)
                        }

                        INSTANSE.editor?.commit()

                    }
                    appWidgetManager.updateAppWidget(widgetId, remoteViews)
                }
            }).start()
        }
    }

}
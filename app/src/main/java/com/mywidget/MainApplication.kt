package com.mywidget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.RemoteViews
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB

class MainApplication : Application() {

    var db: SQLiteDatabase? = null
    val table = "USER"
    val menu_table = "MENU"
    val memo_table = "MEMO"
    val loveday_table = "LOVEDAY"
    var editor: SharedPreferences.Editor? = null
    var mSharedPreference: SharedPreferences? = null
    var googleIdtoken: String? = null
    var userDb: UserDB? = null

    override fun onCreate() {
        super.onCreate()

        userDb = UserDB.getInstance(INSTANSE)
        db = this.openOrCreateDatabase("widgetDb", Context.MODE_PRIVATE, null)
        mSharedPreference = getSharedPreferences("unme", Context.MODE_PRIVATE)
        editor = mSharedPreference?.edit()
    }

    init {
        INSTANSE = this
    }


    companion object {

        lateinit var INSTANSE: MainApplication

        fun loveDaySelect(): String? {
            val cursor = INSTANSE.db?.rawQuery("SELECT * FROM " + INSTANSE.loveday_table, null)
            var date: String? = ""

            try {
                cursor?.moveToFirst()
                if(cursor?.moveToFirst() != null) {
                    date = cursor.getString(0)
                }
                return date
            } catch (e: Exception) {
                return "0"
            }
        }

        fun memoSelect(): ArrayList<ArrayList<String>> {
            val cursor = INSTANSE.db?.rawQuery("SELECT * FROM " + INSTANSE.memo_table, null)

            var itemArray = arrayListOf(arrayListOf<String>())
            itemArray.clear()

            try {
                cursor?.moveToFirst()

                if (cursor?.moveToFirst() != null) {
                    for (i in 0 until cursor.count) {
                        var arraylist = arrayListOf<String>()
                        val name = cursor.getString(0)
                        val phone = cursor.getString(1)

                        arraylist.add(name)
                        arraylist.add(phone)

                        itemArray.add(i, arraylist)

                        cursor.moveToNext()
                    }
                }
                return itemArray
            } catch (e: Exception) {
                return itemArray
            }
        }

        fun memoDelete(keyword: String) {
            INSTANSE.db?.execSQL("delete from " + INSTANSE.memo_table + " where memo = '$keyword'")
        }

        fun widgetBroad() {
            var userList = listOf<User>()
            val r = Runnable {
                try {
                    userList = INSTANSE.userDb?.userDao()?.getUser()!!
                } catch (e: java.lang.Exception) {

                }
            }

            val thread = Thread(r)
            thread.start()

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
        }
    }

}
package com.mywidget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class MyAppWidget : AppWidgetProvider() {

    private var db: SQLiteDatabase? = null
    var mSharedPreference: SharedPreferences? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            mSharedPreference = context.getSharedPreferences("unme", Context.MODE_PRIVATE)
            var remoteViews = RemoteViews(context.packageName, R.layout.my_app_widget)

            //initwidgetView(context, appWidgetManager, appWidgetId)
            Log.d("업데이트2", "요긴가2")
            updateAppWidget(context, appWidgetManager, appWidgetId, mSharedPreference)
            //appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        @SuppressLint("NewApi")
        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int, sharedPreferences: SharedPreferences?
        ) {

            initwidgetView(context, appWidgetManager, appWidgetId, sharedPreferences)

            //updateViews.setTextViewText(R.id.day, todate.toString()+"일")
        }

        fun initwidgetView(context: Context, appWidgetManager: AppWidgetManager,
                           appWidgetId: Int, mSharedPreference: SharedPreferences?) {


            var updateViews = RemoteViews(context.packageName, R.layout.my_app_widget)

            Log.d("업데이트2", "요긴가")

            val number1 = mSharedPreference?.getString("number1", "")
            val number2 = mSharedPreference?.getString("number2", "")
            val number3 = mSharedPreference?.getString("number3", "")
            val number4 = mSharedPreference?.getString("number4", "")
            val number5 = mSharedPreference?.getString("number5", "")

            var intent1 = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number1))
            var pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0)
            var intent2 = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number2))
            var pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0)
            var intent3 = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number3))
            var pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0)
            var intent4 = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number4))
            var pendingIntent4 = PendingIntent.getActivity(context, 0, intent4, 0)
            var intent5 = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number5))
            var pendingIntent5 = PendingIntent.getActivity(context, 0, intent5, 0)

            updateViews.setOnClickPendingIntent(R.id.button1, pendingIntent1)
            updateViews.setOnClickPendingIntent(R.id.button2, pendingIntent2)
            updateViews.setOnClickPendingIntent(R.id.button3, pendingIntent3)
            updateViews.setOnClickPendingIntent(R.id.button4, pendingIntent4)
            updateViews.setOnClickPendingIntent(R.id.button5, pendingIntent5)

            appWidgetManager.updateAppWidget(appWidgetId, updateViews)

        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val manager = AppWidgetManager.getInstance(context)

        this.onUpdate(context!!, manager, manager.getAppWidgetIds(ComponentName(context, MyAppWidget::class.java)))
    }

}


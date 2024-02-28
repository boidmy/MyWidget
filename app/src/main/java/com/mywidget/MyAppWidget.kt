package com.mywidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mywidget.data.room.User
import java.lang.reflect.Type

/**
 * Implementation of App Widget functionality.
 */
class MyAppWidget : AppWidgetProvider() {

    private var mSharedPreference: SharedPreferences? = null
    private val idList = arrayListOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5)
    private val widgetContainer = arrayListOf(R.id.widgetItemContainer1, R.id.widgetItemContainer2,
        R.id.widgetItemContainer3, R.id.widgetItemContainer4, R.id.widgetItemContainer5)
    private val widgetName = arrayListOf(R.id.name1, R.id.name2, R.id.name3, R.id.name4, R.id.name5)

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            mSharedPreference = context.getSharedPreferences("widget", Context.MODE_PRIVATE)
            initWidgetView(context, appWidgetManager, appWidgetId, mSharedPreference)
        }
    }

    private fun initWidgetView(context: Context, appWidgetManager: AppWidgetManager,
                               appWidgetId: Int, mSharedPreference: SharedPreferences?) {

        val json = mSharedPreference?.getString("data", "")
        val listType: Type = object : TypeToken<List<User>>() {}.type
        val widgetData = Gson().fromJson<List<User>>(json, listType)
        val updateViews = addView(context, widgetData)

        appWidgetManager.updateAppWidget(appWidgetId, updateViews)
    }

    private fun actionCall(context: Context, number: String): PendingIntent {
        val intent1 = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        return PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val manager = AppWidgetManager.getInstance(context)
        this.onUpdate(context, manager, manager.getAppWidgetIds(ComponentName(context, MyAppWidget::class.java)))
    }

    private fun addView(context: Context, widgetData: List<User>): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.my_app_widget)

        for(i in 0..4) {
            remoteViews.setViewVisibility(widgetContainer[i], View.GONE)
        }

        widgetData.forEachIndexed { index, user ->
            remoteViews.setTextViewText(widgetName[index], user.name)
            remoteViews.setViewVisibility(widgetContainer[index], View.VISIBLE)
            remoteViews.setOnClickPendingIntent(idList[index], actionCall(context, user.number?:""))
        }
        return remoteViews
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


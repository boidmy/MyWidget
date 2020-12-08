package com.mywidget.ui.widgetlist

import android.app.Dialog
import dagger.Module
import dagger.Provides

@Module
class WidgetListModule {

    @Provides
    fun provideDialog(activity: WidgetListActivity): Dialog {
        return Dialog(activity)
    }
}
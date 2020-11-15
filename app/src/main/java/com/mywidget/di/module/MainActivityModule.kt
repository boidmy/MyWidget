package com.mywidget.di.module

import android.app.Activity
import android.content.Context
import com.mywidget.BackPressAppFinish
import com.mywidget.adapter.TabPagerAdapter
import com.mywidget.view.MainActivity
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideFinishApp(activity: Activity) : BackPressAppFinish{
        return BackPressAppFinish(activity)
    }

    @Provides
    fun provideTabPagerAdapter(activity: Activity) : TabPagerAdapter {
        return TabPagerAdapter((activity as MainActivity).supportFragmentManager)
    }
}
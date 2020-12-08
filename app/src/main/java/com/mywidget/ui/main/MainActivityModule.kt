package com.mywidget.ui.main

import androidx.fragment.app.FragmentManager
import com.mywidget.common.BackPressAppFinish
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import com.mywidget.ui.main.recyclerview.MainTabMemoAdapter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideFinishApp(activity: MainActivity): BackPressAppFinish {
        return BackPressAppFinish(activity)
    }

    @Provides
    fun provideFragmentManager(activity: MainActivity): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun provideTabPagerAdapter(fragmentManager: FragmentManager): MainTabPagerAdapter {
        return MainTabPagerAdapter(fragmentManager)
    }

    @Provides
    fun provideMainTabMemoAdapter(): MainTabMemoAdapter {
        return MainTabMemoAdapter()
    }
}
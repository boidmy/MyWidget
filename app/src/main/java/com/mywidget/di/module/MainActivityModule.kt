package com.mywidget.di.module

import android.app.Activity
import android.content.Context
import com.mywidget.BackPressAppFinish
import com.mywidget.adapter.MainTabMemoAdapter
import com.mywidget.adapter.TabPagerAdapter
import com.mywidget.view.MainActivity
import com.mywidget.viewModel.ViewModelFactory
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

    @Provides
    fun provideMainTabMemoAdapter() : MainTabMemoAdapter {
        return MainTabMemoAdapter()
    }
}
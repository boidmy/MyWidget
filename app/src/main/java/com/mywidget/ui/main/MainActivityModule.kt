package com.mywidget.ui.main

import android.app.Dialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.mywidget.R
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.MainFavoritesDialogBinding
import com.mywidget.databinding.MainLovedayDialogBinding
import com.mywidget.databinding.MemoDialogBinding
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
    fun provideDialog(activity: MainActivity): Dialog {
        return Dialog(activity, R.style.CustomDialogTheme)
    }

    @Provides
    fun provideLoveDayDialogBinding(activity: MainActivity): MainLovedayDialogBinding {
        return MainLovedayDialogBinding.inflate(LayoutInflater.from(activity))
    }

    @Provides
    fun provideMemoDialogBinding(activity: MainActivity): MemoDialogBinding {
        return MemoDialogBinding.inflate(LayoutInflater.from(activity))
    }

    @Provides
    fun provideFavoritesDialogBinding(activity: MainActivity): MainFavoritesDialogBinding {
        return MainFavoritesDialogBinding.inflate(LayoutInflater.from(activity))
    }
}
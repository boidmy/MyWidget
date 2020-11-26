package com.mywidget.di.module

import com.mywidget.ui.main.fragment.FragmentLoveDay
import com.mywidget.ui.main.fragment.FragmentMemo
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun fragmentLoveDay(): FragmentLoveDay

    @ContributesAndroidInjector
    abstract fun fragmentMemo(): FragmentMemo
}
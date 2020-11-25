package com.mywidget.di.module

import com.mywidget.view.fragment.FragmentLoveDay
import com.mywidget.view.fragment.FragmentMemo
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun fragmentLoveDay(): FragmentLoveDay

    @ContributesAndroidInjector
    abstract fun fragmentMemo(): FragmentMemo
}
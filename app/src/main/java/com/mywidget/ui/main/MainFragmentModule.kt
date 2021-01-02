package com.mywidget.ui.main

import com.mywidget.ui.main.fragment.FragmentLoveDay
import com.mywidget.ui.main.fragment.FragmentMemo
import com.mywidget.ui.main.fragment.FragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract fun fragmentLoveDay(): FragmentLoveDay

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun fragmentMemo(): FragmentMemo
}
package com.mywidget.di.compoenet

import android.app.Activity
import com.mywidget.di.ActivityScope
import com.mywidget.di.module.MainActivityModule
import com.mywidget.view.MainActivity
import com.mywidget.view.fragment.FragmentLoveDay
import com.mywidget.view.fragment.FragmentMemo
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(fragmentLoveDay: FragmentLoveDay)
    fun inject(fragmentMemo: FragmentMemo)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity) : MainActivityComponent
    }
}
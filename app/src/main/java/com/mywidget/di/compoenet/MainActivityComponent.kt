package com.mywidget.di.compoenet

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.mywidget.di.ActivityScope
import com.mywidget.di.module.MainActivityModule
import com.mywidget.di.module.MainViewModel
import com.mywidget.view.MainActivity
import com.mywidget.view.fragment.FragmentLoveDay
import com.mywidget.view.fragment.FragmentMemo
import com.mywidget.viewModel.ViewModelFactory
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@ActivityScope
@Subcomponent(modules = [
    MainActivityModule::class,
    MainViewModel::class])
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(fragmentLoveDay: FragmentLoveDay)
    fun inject(fragmentMemo: FragmentMemo)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: Activity) : MainActivityComponent
    }
}
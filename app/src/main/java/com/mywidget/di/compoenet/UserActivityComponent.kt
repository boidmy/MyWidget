package com.mywidget.di.compoenet

import com.mywidget.di.module.MainViewModel
import com.mywidget.view.UserActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainViewModel::class])
interface UserActivityComponent {

    fun inject(userActivity: UserActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create() : UserActivityComponent
    }
}
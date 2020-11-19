package com.mywidget.di.compoenet

import com.mywidget.di.module.MainViewModelModule
import com.mywidget.di.module.UserViewModelModule
import com.mywidget.view.UserActivity
import dagger.Subcomponent

@Subcomponent(modules = [
    UserViewModelModule::class])
interface UserActivityComponent {

    fun inject(userActivity: UserActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create() : UserActivityComponent
    }
}
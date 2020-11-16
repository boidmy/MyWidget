package com.mywidget.di.compoenet

import com.mywidget.view.UserActivity
import dagger.Subcomponent

@Subcomponent
interface UserActivityComponent {

    fun inject(userActivity: UserActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create() : UserActivityComponent
    }
}
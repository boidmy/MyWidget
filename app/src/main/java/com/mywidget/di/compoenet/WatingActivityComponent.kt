package com.mywidget.di.compoenet

import com.mywidget.chat.waiting.WaitingRoomActivity
import com.mywidget.chat.waiting.WatingViewModelModule
import dagger.Subcomponent

@Subcomponent(modules = [
    WatingViewModelModule::class
])
interface WatingActivityComponent {

    fun inject(watingActivity: WaitingRoomActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create() : WatingActivityComponent
    }
}
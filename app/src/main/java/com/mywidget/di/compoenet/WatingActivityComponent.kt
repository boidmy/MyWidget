package com.mywidget.di.compoenet

import com.mywidget.chat.waiting.WaitingRoomActivity
import dagger.Subcomponent

@Subcomponent
interface WatingActivityComponent {

    fun inject(watingActivity: WaitingRoomActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create() : WatingActivityComponent
    }
}
package com.mywidget.di.compoenet

import android.app.Application
import com.mywidget.MainApplication
import com.mywidget.di.module.FactoryModule
import com.mywidget.di.module.RoomModule
import com.mywidget.di.module.SubComponentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    SubComponentModule::class,
    RoomModule::class,
    FactoryModule::class])
interface ApplicationComponent {
    fun mainActivityComponent() : MainActivityComponent.Factory
    fun userActivityComponent() : UserActivityComponent.Factory

    fun inject(application: MainApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : ApplicationComponent
    }
}
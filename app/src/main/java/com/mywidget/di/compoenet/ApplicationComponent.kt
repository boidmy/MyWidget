package com.mywidget.di.compoenet

import android.app.Application
import com.mywidget.MainApplication
import com.mywidget.di.module.ApplicationModule
import com.mywidget.di.module.RoomModule
import com.mywidget.di.module.SubComponentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SubComponentModule::class,
    RoomModule::class])
interface ApplicationComponent {
    val mainActivityComponentBuilder : MainActivityComponent.Factory

    fun inject(application: MainApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : ApplicationComponent
    }
}
package com.mywidget.di.compoenet

import com.mywidget.MainApplication
import com.mywidget.di.module.ApplicationModule
import com.mywidget.di.module.SubComponentModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    SubComponentModule::class])
interface ApplicationComponent {
    val mainActivityComponentBuilder : MainActivityComponent.Factort

    fun inject(application: MainApplication)

    @Component.Factory
    interface Factory {
        fun create() : ApplicationComponent
    }
}
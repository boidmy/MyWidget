package com.mywidget.di.module

import com.mywidget.di.compoenet.MainActivityComponent
import dagger.Module

@Module(subcomponents = [MainActivityComponent::class])
class SubComponentModule {
}
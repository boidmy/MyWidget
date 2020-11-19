package com.mywidget.di.module

import android.app.Application
import com.mywidget.di.compoenet.MainActivityComponent
import com.mywidget.di.compoenet.UserActivityComponent
import com.mywidget.repository.MessageRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [
    MainActivityComponent::class,
    UserActivityComponent::class])
class SubComponentModule {}
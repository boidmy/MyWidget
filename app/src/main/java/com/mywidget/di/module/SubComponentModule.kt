package com.mywidget.di.module

import android.app.Application
import com.mywidget.di.compoenet.ChattingActivityComponent
import com.mywidget.di.compoenet.MainActivityComponent
import com.mywidget.di.compoenet.UserActivityComponent
import com.mywidget.di.compoenet.WatingActivityComponent
import com.mywidget.repository.MessageRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [
    MainActivityComponent::class,
    UserActivityComponent::class,
    WatingActivityComponent::class,
    ChattingActivityComponent::class])
class SubComponentModule {}
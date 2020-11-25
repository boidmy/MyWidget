package com.mywidget.di.module

import com.mywidget.chat.chatting.ChatActivity
import com.mywidget.chat.chatting.ChatModule
import com.mywidget.chat.waiting.WaitingRoomActivity
import com.mywidget.chat.waiting.WatingViewModelModule
import com.mywidget.di.ActivityScope
import com.mywidget.view.MainActivity
import com.mywidget.view.UserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            MainActivityModule::class,
            MainFragmentModule::class,
            MainViewModelModule::class]
    )
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [UserViewModelModule::class])
    abstract fun userActivity(): UserActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [WatingViewModelModule::class])
    abstract fun watingRoomActivity(): WaitingRoomActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun chattingActivity(): ChatActivity
}
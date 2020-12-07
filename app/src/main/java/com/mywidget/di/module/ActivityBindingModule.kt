package com.mywidget.di.module

import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chat.ChatModule
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.ui.chatroom.ChatRoomViewModelModule
import com.mywidget.di.custom.ActivityScope
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.login.LoginGoogleModule
import com.mywidget.ui.main.MainActivity
import com.mywidget.ui.main.MainActivityModule
import com.mywidget.ui.login.signup.SignUpActivity
import com.mywidget.ui.login.LoginViewModelModule
import com.mywidget.ui.widgetlist.WidgetListActivity
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
    abstract fun userActivity(): WidgetListActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChatRoomViewModelModule::class])
    abstract fun watingRoomActivity(): ChatRoomActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChatModule::class])
    abstract fun chattingActivity(): ChatActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        LoginGoogleModule::class,
        LoginViewModelModule::class])
    abstract fun loginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LoginViewModelModule::class])
    abstract fun signUpActivity(): SignUpActivity
}
package com.mywidget.di.module

import com.mywidget.ui.chat.ChatActivity
import com.mywidget.ui.chat.ChatModule
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.ui.chatroom.ChatRoomViewModelModule
import com.mywidget.di.custom.ActivityScope
import com.mywidget.ui.chatinvite.ChatInviteActivity
import com.mywidget.ui.chatinvite.ChatInviteModule
import com.mywidget.ui.chatroom.ChatRoomModule
import com.mywidget.ui.friend.FriendActivity
import com.mywidget.ui.friend.FriendModule
import com.mywidget.ui.friend.FriendViewModelModule
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.login.LoginGoogleModule
import com.mywidget.ui.main.MainActivity
import com.mywidget.ui.main.MainActivityModule
import com.mywidget.ui.login.signup.SignUpActivity
import com.mywidget.ui.login.LoginViewModelModule
import com.mywidget.ui.loveday.FloatingPopupActivity
import com.mywidget.ui.loveday.FloatingPopupModule
import com.mywidget.ui.main.MainFragmentModule
import com.mywidget.ui.main.MainViewModelModule
import com.mywidget.ui.mypage.MyPageActivity
import com.mywidget.ui.mypage.MyPageViewModelModule
import com.mywidget.ui.widgetlist.WidgetListViewModelModule
import com.mywidget.ui.widgetlist.WidgetListActivity
import com.mywidget.ui.widgetlist.WidgetListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        MainFragmentModule::class,
        MainViewModelModule::class
    ])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        WidgetListViewModelModule::class,
        WidgetListModule::class
    ])
    abstract fun widgetListActivity(): WidgetListActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        ChatRoomViewModelModule::class,
        ChatRoomModule::class])
    abstract fun chatRoomActivity(): ChatRoomActivity

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

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        FriendViewModelModule::class,
        FriendModule::class])
    abstract fun friendActivity(): FriendActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChatInviteModule::class])
    abstract fun inviteActivity(): ChatInviteActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MyPageViewModelModule::class])
    abstract fun myPageActivity(): MyPageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FloatingPopupModule::class])
    abstract fun floatingPopupActivity(): FloatingPopupActivity
}
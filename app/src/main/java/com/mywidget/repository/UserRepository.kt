package com.mywidget.repository

import android.app.Application
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB

class UserRepository(application: Application) {

    private val userDb = UserDB.getInstance(application)

    fun insertUser(user: String, phone: String) {
        userDb?.userDao()?.insert(User(null, user, phone))
    }

    fun deleteUser(user: String) {
        userDb?.userDao()?.delete(user)
    }

    fun selectUser() : List<User>? {
        return userDb?.userDao()?.getUser()
    }
}
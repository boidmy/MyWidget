package com.mywidget.ui.widgetlist

import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import javax.inject.Inject

class WidgetListRepository @Inject constructor(
    private val userDb: UserDB) {

    fun insertUser(user: String, phone: String) {
        userDb.userDao().insert(User(null, user, phone))
    }

    fun deleteUser(user: String) {
        userDb.userDao().delete(user)
    }

    fun selectUser() : List<User>? {
        return userDb.userDao().getUser()
    }
}
package com.mywidget.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB

class UserViewModel(application: Application) : AndroidViewModel(application) {

    var data: MutableLiveData<List<User>> = MutableLiveData()

    fun insertUser(user: String, phone: String, userDB: UserDB?) {
        userDB?.userDao()?.insert(User(null, user, phone))
        selectUser(userDB)
    }

    fun selectUser(userDB: UserDB?) {
        var userList: List<User>?
        userList = userDB?.userDao()?.getUser()!!
        data.postValue(userList)
    }
}
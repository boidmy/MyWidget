package com.mywidget.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB

class UserViewModel(application: Application) : AndroidViewModel(application) {

    var data: MutableLiveData<List<User>> = MutableLiveData()
    var userDB: UserDB? = null

    fun insertUser(user: String, phone: String) {
        userDB?.userDao()?.insert(User(null, user, phone))
        selectUser()
    }

    fun deleteUser(user: String) {
        userDB?.userDao()?.delete(user)
        selectUser()
    }

    fun selectUser() {
        val userList = userDB?.userDao()?.getUser()!!
        data.postValue(userList)
    }
}
package com.mywidget.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import com.mywidget.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    var data: MutableLiveData<List<User>> = MutableLiveData()
    var dialogVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val repository = UserRepository(application)

    fun insertUser(user: String, phone: String) {
        Thread(Runnable {
            repository.insertUser(user, phone)
            selectUser()
        }).start()
    }

    fun deleteUser(user: String) {
        Thread(Runnable {
            repository.deleteUser(user)
            selectUser()
        }).start()
    }

    fun selectUser() {
        data.postValue(repository.selectUser())
    }
}
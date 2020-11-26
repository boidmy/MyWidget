package com.mywidget.ui.widgetlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.room.User
import com.mywidget.repository.UserRepository
import javax.inject.Inject

class WidgetListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var data: MutableLiveData<List<User>> = MutableLiveData()
    var dialogVisible: MutableLiveData<Boolean> = MutableLiveData()

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
package com.mywidget.ui.widgetlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.room.User
import org.json.JSONArray
import javax.inject.Inject

class WidgetListViewModel @Inject constructor(
    private val repository: WidgetListRepository
) : ViewModel() {

    var data: MutableLiveData<List<User>> = MutableLiveData()
    var dialogVisible: MutableLiveData<Boolean> = MutableLiveData()
    var userEmail: MutableLiveData<String> = MutableLiveData()
    var widgetJsonArrayData: MutableLiveData<JSONArray> = MutableLiveData()

    fun insertUser(user: String, phone: String) {
        repository.insertUser(user, phone)
        setDialogVisibility(false)
    }

    fun deleteUser(user: String) {
        repository.deleteUser(user)
    }

    fun setWidgetData() {
        data = repository.setWidgetData()
    }

    fun setWidgetDataJsonArray() {
        widgetJsonArrayData = repository.setWidgetDataJsonArray()
    }

    fun setDialogVisibility(flag: Boolean) {
        dialogVisible.value = flag
    }

    fun selectUser() {
        repository.selectUser()
    }
}
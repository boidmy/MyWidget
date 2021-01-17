package com.mywidget.ui.widgetlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.room.User
import org.json.JSONArray
import javax.inject.Inject

class WidgetListViewModel @Inject constructor(
    private val repository: WidgetListRepository
) : ViewModel() {

    private var _data: MutableLiveData<List<User>> = MutableLiveData()
    private val _dialogVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val _userEmail: MutableLiveData<String> = MutableLiveData()
    private var _widgetJsonArrayData: MutableLiveData<JSONArray> = MutableLiveData()
    private val _deleteWidget: MutableLiveData<User> = MutableLiveData()
    private val _deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val data: MutableLiveData<List<User>>
        get() = _data

    val dialogVisible: MutableLiveData<Boolean>
        get() = _dialogVisible

    val userEmail: MutableLiveData<String>
        get() = _userEmail

    val widgetJsonArrayData: MutableLiveData<JSONArray>
        get() = _widgetJsonArrayData

    val deleteWidget: MutableLiveData<User>
        get() = _deleteWidget

    val deleteDialogVisibility: MutableLiveData<Boolean>
        get() = _deleteDialogVisibility

    fun insertUser(user: String, phone: String) {
        repository.insertUser(user, phone)
        setDialogVisibility(false)
    }

    fun deleteUser(seq: Int) {
        repository.deleteUser(seq)
        deleteDialogVisibility(false)
    }

    fun setWidgetData() {
        _data = repository.setWidgetData()
    }

    fun setWidgetDataJsonArray() {
        _widgetJsonArrayData = repository.setWidgetDataJsonArray()
    }

    fun setDialogVisibility(flag: Boolean) {
        dialogVisible.value = flag
    }

    fun selectUser() {
        repository.selectUser()
    }

    fun setDeleteWidget(data: User) {
        deleteWidget.value = data
    }

    fun deleteDialogVisibility(flag: Boolean) {
        deleteDialogVisibility.value = flag
    }
}
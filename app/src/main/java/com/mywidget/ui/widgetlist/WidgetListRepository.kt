package com.mywidget.ui.widgetlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class WidgetListRepository @Inject constructor(private val userDb: UserDB) {

    var data: MutableLiveData<List<User>> = MutableLiveData()
    var widgetJsonArrayData: MutableLiveData<JSONArray> = MutableLiveData()

    fun insertUser(user: String, phone: String) {
        Thread {
            userDb.userDao().insert(User(null, user, phone))
            selectUser()
        }.start()
    }

    fun deleteUser(seq: Int) {
        Thread {
            userDb.userDao().delete(seq)
            selectUser()
        }.start()
    }

    fun selectUser() {
        val list = userDb.userDao().getUser()
        data.postValue(list)
        widgetJsonArrayData.postValue(parseArray(list))
    }

    fun setWidgetData(): MutableLiveData<List<User>> {
        return data
    }

    fun setWidgetDataJsonArray(): MutableLiveData<JSONArray> {
        return widgetJsonArrayData
    }

    private fun parseArray(list: List<User>): JSONArray {
        val jsonArray = JSONArray()
        for (input in list) {
            val jsonObject = JSONObject()
            jsonObject.put("name", input.name)
            jsonObject.put("number", input.number)
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }
}
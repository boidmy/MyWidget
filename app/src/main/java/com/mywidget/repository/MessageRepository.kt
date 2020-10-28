package com.mywidget.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.Util
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.LoveDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MessageRepository {

    private var unSubscripbe: CompositeDisposable = CompositeDisposable()
    private var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    private var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    companion object {

        var INSTANCE = MessageRepository()

        fun instance(): MessageRepository {
            return INSTANCE
        }
    }

    fun messageLeft(name: String) : MutableLiveData<List<LmemoData>> {
        unSubscripbe.add(
            ApiConnection.Instance().retrofitService
                .lmemoData(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { item ->
                        leftMessage.value = getGsonMessage(item)
                    }, {
                })
        )
        return leftMessage
    }

    fun messageRight(name: String) : MutableLiveData<List<LmemoData>> {
        unSubscripbe.add(
            ApiConnection.Instance().retrofitService
                .lmemoData(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { item ->
                        rightMessage.value = getGsonMessage(item)
                    }, {
                })
        )
        return rightMessage
    }

    fun getGsonMessage(jsonObject: JsonObject) : List<LmemoData> {
        val obj = JSONObject(Gson().toJson(jsonObject))
        val x = obj.keys()
        val array = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            array.put(obj.get(key))
        }

        return Gson().fromJson(array.toString(), object: TypeToken<ArrayList<LmemoData>>(){}.type)
    }

    fun lovedayFormatt(data: List<LoveDay>?): String {
        return Util.howMuchloveDay(data?.get(data.size-1)?.date)
    }

    fun rxClear() {
        unSubscripbe.dispose()
    }
}
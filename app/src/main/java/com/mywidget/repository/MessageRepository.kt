package com.mywidget.repository

import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.CalendarUtil
import com.mywidget.Util
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.LoveDay
import com.mywidget.data.room.LoveDayDB
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.databinding.MainFragmentRvItemBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

class MessageRepository(application: Application) {

    private var unSubscripbe: CompositeDisposable = CompositeDisposable()
    private var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    private var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    private var memoDb = MemoDB.getInstance(application)
    private var loveDayDb = LoveDayDB.getInstance(application)

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
        data?.let {
            return if(it.isNotEmpty()) {
                Util.howMuchloveDay(it[data.size-1].date)
            } else "0"
        } ?: run {
            return "0"
        }
    }

    fun addLoveDay(data: String) {
        loveDayDb?.loveDayDao()?.insert(LoveDay(null, data))
    }

    fun selectLoveDay() : String {
        return lovedayFormatt(loveDayDb?.loveDayDao()?.getData())
    }

    fun deleteMemo(memo: String) {
        memoDb.memoDao().delete(memo)
    }

    fun selectMemo() : List<Memo>? {
        return memoDb.memoDao().getUser()
    }

    fun insertMemo(memo: String, data: String) {
        memoDb.memoDao().insert(Memo(null, memo, data))
    }

    fun rxClear() {
        memoDb.destroyInstance()
        loveDayDb?.destroyInstance()
        unSubscripbe.dispose()
    }
}
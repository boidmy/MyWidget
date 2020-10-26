package com.mywidget.viewModel

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.*
import com.mywidget.lmemo.Ldata
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var data: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<List<LoveDay>> = MutableLiveData()
    var leftMessage: MutableLiveData<List<Ldata>> = MutableLiveData()
    var rightMessage: MutableLiveData<List<Ldata>> = MutableLiveData()
    var message: MutableLiveData<List<LmemoData>> = MutableLiveData()
    
    var memoDB: MemoDB? = null
    var loveDayDB: LoveDayDB? = null
    private var unSubscripbe: CompositeDisposable = CompositeDisposable()

    fun insertMemo(memo: String, data: String) {
        memoDB?.memoDao()?.insert(Memo(null, memo, data))
        selectMemo()
    }

    fun deletMemo(memo: String) {
        memoDB?.memoDao()?.delete(memo)
        selectMemo()
    }

    fun selectMemo() {
        data.postValue(memoDB?.memoDao()?.getUser())
    }

    fun insertLoveDay(data: String) {
        loveDayDB?.loveDayDao()?.insert(LoveDay(null, data))
        selectLoveDay()
    }

    fun selectLoveDay() {
        loveday.postValue(loveDayDB?.loveDayDao()?.getData())
    }

    fun message(database: DatabaseReference) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                var arrayRight = arrayListOf<Ldata>()
                var arrayLeft = arrayListOf<Ldata>()

                for (ds in data.child("콩이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    product?.let {
                        arrayRight.add(it)
                    }
                }

                for (ds in data.child("뿡이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    product?.let {
                        arrayLeft.add(it)
                    }
                }

                leftMessage.value = arrayLeft
                rightMessage.value = arrayRight
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
    
    fun leftClick() {
        unSubscripbe.add(
            ApiConnection.Instance().retrofitService
                .lmemoData("뿡이")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { item ->
                        getGsonMessage(item)
                    }, {exception ->
                        Log.d("error!", exception.toString())
                    })
        )
    }
    
    fun rightClick() {
        unSubscripbe.add(
            ApiConnection.Instance().retrofitService
                .lmemoData("콩이")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    { item ->
                        getGsonMessage(item)
                    }, {exception ->
                        Log.d("error!", exception.toString())
                    })
        )
    }

    fun getGsonMessage(jsonObject: JsonObject) {
        val obj = JSONObject(Gson().toJson(jsonObject))
        val x = obj.keys()
        val array = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            array.put(obj.get(key))
        }
        val arrayLmemoData: List<LmemoData> = Gson().fromJson(array.toString(), object:
            TypeToken<ArrayList<LmemoData>>(){}.type)

        message.value = arrayLmemoData
    }

    fun rxClear() {
        unSubscripbe.dispose()
    }
}
package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.LoveDay
import com.mywidget.data.room.LoveDayDB
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.di.custom.ActivityScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import util.CalendarUtil.howMuchloveDay
import util.Util
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope
class MainRepository @Inject constructor(
    private val memoDb: MemoDB, private val loveDayDb: LoveDayDB) {

    @Inject lateinit var database: DatabaseReference
    private var unSubscripbe: CompositeDisposable = CompositeDisposable()
    private var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    private var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val friendRef: DatabaseReference by lazy {
        userRef.child(Util.replacePointToComma(myId.value ?:"")).child("friend") }
    var myId: MutableLiveData<String> = MutableLiveData()
    private val favorites: DatabaseReference by lazy {
        database.child("favorites").child(Util.replacePointToComma(myId.value ?:"")) }
    private val favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()

    fun favoritesMessageMe(name: String) : MutableLiveData<List<LmemoData>> {
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

    fun favoritesMessageFriend(name: String) : MutableLiveData<List<LmemoData>> {
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

    private fun getGsonMessage(jsonObject: JsonObject) : List<LmemoData> {
        val obj = JSONObject(Gson().toJson(jsonObject))
        val x = obj.keys()
        val array = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            array.put(obj.get(key))
        }

        return Gson().fromJson(array.toString(), object: TypeToken<ArrayList<LmemoData>>(){}.type)
    }

    private fun lovedayFormatt(data: List<LoveDay>?): String {
        data?.let {
            return if(it.isNotEmpty()) {
                howMuchloveDay(it[data.size-1].date)
            } else "0"
        } ?: run {
            return "0"
        }
    }

    fun addLoveDay(data: String) {
        loveDayDb.loveDayDao().insert(LoveDay(null, data))
    }

    fun selectLoveDay() : String {
        return lovedayFormatt(loveDayDb.loveDayDao().getData())
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

    fun favoritesMessage(text: String) {
        friendRef.child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    favoritesExistence.value = false
                } else {
                    favorites.child(snapshot.value.toString()).push().setValue(text)
                    favoritesExistence.value = true
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun logout(email: String) {
        userRef.child(Util.replacePointToComma(email)).child("token").setValue(null)
    }

    fun myId(email: String) {
        myId.value = email
    }

    fun myIdReset(): MutableLiveData<String>{
        return myId
    }

    fun favoritesExistence(): MutableLiveData<Boolean> {
        return favoritesExistence
    }

    fun rxClear() {
        unSubscripbe.dispose()
    }
}
package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.FavoritesData
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
import util.Util.replacePointToComma
import java.util.ArrayList
import javax.inject.Inject

@ActivityScope
class MainRepository @Inject constructor(
    private val memoDb: MemoDB, private val loveDayDb: LoveDayDB) {

    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val friendRef: DatabaseReference by lazy {
        userRef.child(replacePointToComma(myId.value ?:"")).child("friend") }
    var myId: MutableLiveData<String> = MutableLiveData()
    private val favoritesRef: DatabaseReference by lazy {
        database.child("favorites") }
    private val favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistenceMyFriend: MutableLiveData<String> = MutableLiveData()
    private val favoritesMessageMe: MutableLiveData<FavoritesData> = MutableLiveData()
    private val favoritesMessageFriend: MutableLiveData<FavoritesData> = MutableLiveData()

    fun favoritesExistenceMyFriend() : MutableLiveData<String> {
        friendRef.child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.value != null) {
                        favoritesExistenceMyFriend.value = snapshot.value.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
        })
        return favoritesExistenceMyFriend
    }

    fun favoritesMessageMe(email: String) {
        favoritesRef.child(replacePointToComma(myId.value ?:""))
            .child(replacePointToComma(email)).limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap: DataSnapshot in snapshot.children) {
                        favoritesMessageMe.value =
                            FavoritesData("", snap.value.toString(), "")
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
        })
    }


    fun favoritesMessageFriend(email: String) {
        favoritesRef.child(replacePointToComma(email))
            .child(replacePointToComma(myId.value ?:"")).limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap: DataSnapshot in snapshot.children) {
                        favoritesMessageFriend.value =
                            FavoritesData("", snap.value.toString(), "")
                    }
                }
                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun favoritesResetMe(): MutableLiveData<FavoritesData> {
        return favoritesMessageMe
    }

    fun favoritesResetFriend(): MutableLiveData<FavoritesData> {
        return favoritesMessageFriend
    }

    private fun getGsonMessage(jsonObject: JsonObject) : List<FavoritesData> {
        val obj = JSONObject(Gson().toJson(jsonObject))
        val x = obj.keys()
        val array = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            array.put(obj.get(key))
        }

        return Gson().fromJson(array.toString(), object: TypeToken<ArrayList<FavoritesData>>(){}.type)
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
                    favoritesRef.child(replacePointToComma(myId.value ?:""))
                        .child(snapshot.value.toString()).push().setValue(text)
                    favoritesExistence.value = true
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun logout(email: String) {
        userRef.child(replacePointToComma(email)).child("token").setValue(null)
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
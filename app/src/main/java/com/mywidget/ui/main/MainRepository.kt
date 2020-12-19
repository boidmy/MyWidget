package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.model.UserData
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
import util.CalendarUtil
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
    var myId: MutableLiveData<String> = MutableLiveData()
    private val favoritesRef: DatabaseReference by lazy {
        database.child("favorites") }
    private val favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistenceMyFriend: MutableLiveData<UserData> = MutableLiveData()
    private val favoritesMessageMe: MutableLiveData<FavoritesData> = MutableLiveData()
    private val favoritesMessageFriend: MutableLiveData<FavoritesData> = MutableLiveData()

    fun favoritesNoneMessageMe() {
        favoritesMessageMe.value = null
    }

    fun favoritesNoneMessageFriend() {
        favoritesMessageFriend.value = null
    }

    fun favoritesResetMe(): MutableLiveData<FavoritesData> {
        return favoritesMessageMe
    }

    fun favoritesResetFriend(): MutableLiveData<FavoritesData> {
        return favoritesMessageFriend
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

    fun logout(email: String) {
        userRef.child(replacePointToComma(email)).child("token").setValue(null)
    }

    fun myId(email: String) {
        myId.value = email
    }

    fun myIdReset(): MutableLiveData<String>{
        return myId
    }

    fun favoritesExistenceMyFriend() : MutableLiveData<UserData> {
        userRef.child(replacePointToComma(myId.value ?:"")).child("friend")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("favorites").value.toString().isNotEmpty()) {
                        val friendEmail: String = snapshot.child("favorites").value.toString()
                        val friendNickName = snapshot.child("friendList").child(
                            replacePointToComma(friendEmail)).value.toString()
                        favoritesExistenceMyFriend.value = UserData(
                            friendEmail,"","",friendNickName)
                    } else {
                        favoritesExistenceMyFriend.value = null
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
        })
        return favoritesExistenceMyFriend
    }

    fun favoritesMessageMe(friendEmail: String) {
        favoritesRef.child(replacePointToComma(myId.value ?:""))
            .child(replacePointToComma(friendEmail)).limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) favoritesNoneMessageMe()
                    for (snap: DataSnapshot in snapshot.children) {
                        val value = snap.getValue(FavoritesData::class.java)
                        favoritesMessageMe.value = value
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun favoritesMessageFriend(friendEmail: String) {
        favoritesRef.child(replacePointToComma(friendEmail))
            .child(replacePointToComma(myId.value ?:"")).limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) favoritesNoneMessageFriend()
                    for (snap: DataSnapshot in snapshot.children) {
                        favoritesMessageFriend.value =
                            FavoritesData("", snap.value.toString(), "")
                    }
                }
                override fun onCancelled(error: DatabaseError) {}

            })
    }

    fun favoritesInsertMessage(message: String) {
        userRef.child(replacePointToComma(myId.value ?:""))
            .child("friend").child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    favoritesExistence.value = false
                } else {
                    favoritesRef.child(replacePointToComma(myId.value ?:""))
                        .child(snapshot.value.toString()).push().setValue(
                            FavoritesData(CalendarUtil.getDate(), message, "")
                        )
                    favoritesExistence.value = true
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun favoritesExistence(): MutableLiveData<Boolean> {
        return favoritesExistence
    }
}
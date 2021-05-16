package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.UserData
import com.mywidget.data.room.LoveDay
import com.mywidget.data.room.LoveDayDB
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.di.custom.ActivityScope
import com.mywidget.extension.favoritesMessageExtension
import util.CalendarUtil
import util.CalendarUtil.howMuchLoveDay
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainRepository @Inject constructor(
    private val memoDb: MemoDB, private val loveDayDb: LoveDayDB) {

    @Inject lateinit var database: DatabaseReference
    @Inject @Named("User") lateinit var userRef: DatabaseReference
    @Inject @Named("favorites") lateinit var favoritesRef: DatabaseReference
    var myId: MutableLiveData<String> = MutableLiveData()
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

    private fun loveDayFormat(data: List<LoveDay>?): String {
        data?.let {
            return if(it.isNotEmpty()) {
                howMuchLoveDay(it[data.size-1].date)
            } else "0"
        } ?: run {
            return "0"
        }
    }

    fun addLoveDay(data: String) {
        loveDayDb.loveDayDao().insert(LoveDay(null, data))
    }

    fun selectLoveDay() : String {
        return loveDayFormat(loveDayDb.loveDayDao().getData())
    }

    fun deleteMemo(data: Memo) {
        memoDb.memoDao().delete(data)
    }

    fun updateMemo(data: Memo, updateMemo: String) {
        val copyData = data.copy(data.sequence, updateMemo, data.date)
        memoDb.memoDao().update(copyData)
    }

    fun selectMemo() : List<Memo>? {
        return memoDb.memoDao().getUser()
    }

    fun insertMemo(memo: Memo) {
        memoDb.memoDao().insert(memo)
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
                    val childFavorites = snapshot.child("favorites").value
                    if (childFavorites != null) {
                        val favoriteFriendEmail: String = childFavorites.toString()
                        val friendModel = snapshot.child("friendList")
                            .child(replacePointToComma(favoriteFriendEmail)).getValue(FriendModel::class.java)
                        favoritesExistenceMyFriend.value =
                            UserData(favoriteFriendEmail,"","", friendModel?.nickName?:"친구")
                    } else {
                        favoritesExistenceMyFriend.value = UserData()
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
        })
        return favoritesExistenceMyFriend
    }

    fun favoritesMessageMe(friendEmail: String) {
        favoritesMessageExtension(favoritesRef.child(replacePointToComma(myId.value ?:""))
            .child(replacePointToComma(friendEmail)), favoritesMessageMe)
    }

    fun favoritesMessageFriend(friendEmail: String) {
        favoritesMessageExtension(favoritesRef.child(replacePointToComma(friendEmail))
            .child(replacePointToComma(myId.value ?:"")), favoritesMessageFriend)
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
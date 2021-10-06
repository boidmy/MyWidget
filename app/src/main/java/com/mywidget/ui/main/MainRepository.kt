package com.mywidget.ui.main

import androidx.lifecycle.LiveData
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import util.CalendarUtil
import util.CalendarUtil.howMuchLoveDay
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class MainRepository @Inject constructor(
    private val memoDb: MemoDB, private val loveDayDb: LoveDayDB
) {

    @Inject
    lateinit var database: DatabaseReference

    @Inject
    @Named("User")
    lateinit var userRef: DatabaseReference

    @Inject
    @Named("favorites")
    lateinit var favoritesRef: DatabaseReference
    private val _myId: MutableLiveData<String> = MutableLiveData()
    private val _favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistenceMyFriend: MutableLiveData<UserData> = MutableLiveData()
    private val favoritesMessageMe: MutableLiveData<FavoritesData> = MutableLiveData()
    private val favoritesMessageFriend: MutableLiveData<FavoritesData> = MutableLiveData()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    val myId: MutableLiveData<String>
        get() = _myId

    val favoritesExistence: LiveData<Boolean>
        get() = _favoritesExistence

    private fun loveDayFormat(data: List<LoveDay>?): String {
        data?.let {
            return if (it.isNotEmpty()) {
                howMuchLoveDay(it[data.size - 1].date)
            } else "0"
        } ?: run {
            return "0"
        }
    }

    suspend fun addLoveDay(data: String) {
        withContext(ioDispatcher) {
            loveDayDb.loveDayDao().insert(LoveDay(null, data))
        }
    }

    suspend fun selectLoveDay() = withContext(ioDispatcher) {
        loveDayFormat(loveDayDb.loveDayDao().getData())
    }

    suspend fun deleteMemo(data: Memo) {
        withContext(ioDispatcher) {
            memoDb.memoDao().delete(data)
        }
    }

    suspend fun updateMemo(data: Memo, updateMemo: String) {
        withContext(ioDispatcher) {
            val copyData = data.copy(data.sequence, updateMemo, data.date)
            memoDb.memoDao().update(copyData)
        }
    }

    suspend fun selectMemo() = withContext(ioDispatcher) {
        memoDb.memoDao().getUser()
    }

    suspend fun insertMemo(memo: Memo) {
        withContext(ioDispatcher) {
            memoDb.memoDao().insert(memo)
        }
    }

    fun logout(email: String) {
        userRef.child(replacePointToComma(email)).child("token").setValue(null)
    }

    fun myId(email: String) {
        _myId.value = email
    }

    fun favoritesExistenceMyFriend(): MutableLiveData<UserData> {
        userRef.child(replacePointToComma(_myId.value ?: "")).child("friend")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val childFavorites = snapshot.child("favorites").value
                    if (childFavorites != null) {
                        val favoriteFriendEmail: String = childFavorites.toString()
                        val friendModel = snapshot.child("friendList")
                            .child(replacePointToComma(favoriteFriendEmail))
                            .getValue(FriendModel::class.java)
                        favoritesExistenceMyFriend.value =
                            UserData(favoriteFriendEmail, "", "", friendModel?.nickName ?: "친구")
                    } else {
                        favoritesExistenceMyFriend.value = UserData()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return favoritesExistenceMyFriend
    }

    fun favoritesMessageMe(friendEmail: String): MutableLiveData<FavoritesData> {
        favoritesMessageExtension(
            favoritesRef.child(replacePointToComma(_myId.value ?: ""))
                .child(replacePointToComma(friendEmail)), favoritesMessageMe
        )

        return favoritesMessageMe
    }

    fun favoritesMessageFriend(friendEmail: String): LiveData<FavoritesData> {
        favoritesMessageExtension(
            favoritesRef.child(replacePointToComma(friendEmail))
                .child(replacePointToComma(_myId.value ?: "")), favoritesMessageFriend
        )
        return favoritesMessageFriend
    }

    fun favoritesInsertMessage(message: String) {
        userRef.child(replacePointToComma(_myId.value ?: ""))
            .child("friend").child("favorites")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        _favoritesExistence.value = false
                    } else {
                        favoritesRef.child(replacePointToComma(_myId.value ?: ""))
                            .child(snapshot.value.toString()).push().setValue(
                                FavoritesData(CalendarUtil.getDate(), message, "")
                            )
                        _favoritesExistence.value = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
package com.mywidget.ui.friend

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.FriendModel
import com.mywidget.extension.friendListExtension
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

class FriendRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    @Inject @Named("User") lateinit var userRef: DatabaseReference
    private val friendRef: DatabaseReference by lazy {
        userRef.child(replacePointToComma(myId)).child("friend") }
    var userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    var friendList: MutableLiveData<List<FriendModel>> = MutableLiveData()
    var myId: String = ""
    var friendUpdateModel: MutableLiveData<FriendModel> = MutableLiveData()
    var friendUpdateDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    fun setUserExistenceChk(): MutableLiveData<Boolean> {
        return userExistenceChk
    }

    fun userExistenceChk(email: String, myId: String, explanation: String) {
        userRef.child(replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val userEmail: String = snapshot.child("email").value.toString()
                        userRef.child(replacePointToComma(myId)).child("friend")
                            .child("friendList").child(replacePointToComma(userEmail))
                            .setValue(FriendModel(replacePointToComma(userEmail), explanation))
                        userExistenceChk.value = true
                    } else {
                        userExistenceChk.value = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun selectFriendList(): MutableLiveData<List<FriendModel>> {
        friendListExtension(friendRef, friendList)
        return friendList
    }

    fun deleteFriend(email: String) {
        friendRef.child("friendList").child(replacePointToComma(email)).setValue(null)
    }

    fun setFavorites(email: String, onOffChk: Boolean) {
        var value: String? = null
        if (onOffChk) value = replacePointToComma(email)
        friendRef.child("favorites").setValue(value)
    }

    fun friendUpdateSelect(email: String): MutableLiveData<FriendModel> {
        userRef.child(replacePointToComma(myId)).child("friend")
            .child("friendList").child(replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val friendModel = snapshot.getValue(FriendModel::class.java)
                    friendUpdateModel.value = friendModel
                    friendUpdateDialogVisibility.value = true
                }
                override fun onCancelled(error: DatabaseError) {}

            })
        return friendUpdateModel
    }

    fun setFriendUpdateDialogVisibility(): MutableLiveData<Boolean> {
        return friendUpdateDialogVisibility
    }

    fun friendUpdate(email: String, nickName: String) {
        userRef.child(replacePointToComma(myId)).child("friend")
            .child("friendList").child(replacePointToComma(email))
            .child("nickName").setValue(nickName)
        friendUpdateDialogVisibility.value = false
    }

    fun myId(email: String): String {
        myId = email
        return myId
    }
}
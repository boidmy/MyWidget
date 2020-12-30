package com.mywidget.ui.friend

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.FriendModel
import util.Util
import util.Util.replaceCommaToPoint
import util.Util.replacePointToComma
import javax.inject.Inject

class FriendRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val friendRef: DatabaseReference by lazy {
        userRef.child(replacePointToComma(myId)).child("friend") }
    private val favorites: DatabaseReference by lazy {
        database.child("favorites").child(replacePointToComma(myId)) }
    var userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()
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

    fun selectFriendList(): MutableLiveData<ArrayList<FriendModel>> {
        friendRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favorites = snapshot.child("favorites").value

                    val array = arrayListOf<FriendModel>()
                    for (snap: DataSnapshot in snapshot.child("friendList").children) {
                        val friendModel = snap.getValue(FriendModel::class.java)
                        friendModel?.let {
                            friendModel.favorites = friendModel.email == favorites
                            array.add(friendModel)
                        }
                    }
                    friendList.value = array
                }
                override fun onCancelled(error: DatabaseError) {}
            })
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
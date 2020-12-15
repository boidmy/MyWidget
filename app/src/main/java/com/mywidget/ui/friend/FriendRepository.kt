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
    @Inject
    lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val friendRef: DatabaseReference by lazy {
        userRef.child(replacePointToComma(myId)).child("friend")
    }
    var userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()
    var myId: String = ""

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
                            .setValue(explanation)
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
                    for (snap: DataSnapshot in snapshot.children) {
                        if(snap.key == "friendList") {
                            for (snapFriend: DataSnapshot in snap.children){
                                val email = snapFriend.key.toString()
                                val explanation = snapFriend.value.toString()
                                var favoritesChk: Boolean
                                favoritesChk = email == favorites
                                array.add(FriendModel(replaceCommaToPoint(email), explanation
                                    , false, favoritesChk))
                            }
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

    fun setFavorites(email: String) {
        friendRef.child("favorites").setValue(replacePointToComma(email))
    }

    fun myId(email: String): String {
        myId = email
        return myId
    }
}
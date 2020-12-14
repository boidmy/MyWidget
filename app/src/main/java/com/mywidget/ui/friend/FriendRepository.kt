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
    var userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()

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
                            .child(replacePointToComma(userEmail)).setValue(explanation)
                        userExistenceChk.value = true
                    } else {
                        userExistenceChk.value = false
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun selectFriendList(myId: String): MutableLiveData<ArrayList<FriendModel>> {
        userRef.child(replacePointToComma(myId)).child("friend")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val array = arrayListOf<FriendModel>()
                    for (snap: DataSnapshot in snapshot.children) {
                        val email = snap.key.toString()
                        val explanation = snap.value.toString()
                        array.add(FriendModel(replaceCommaToPoint(email), explanation))
                    }
                    friendList.value = array
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        return friendList
    }

    fun deleteFriend(myId: String, email: String) {
        userRef.child(replacePointToComma(myId))
            .child("friend").child(replacePointToComma(email)).setValue(null)
    }
}
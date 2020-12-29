package com.mywidget.ui.mypage

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

class MyPageRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    @Inject @Named("User")
    lateinit var userRef: DatabaseReference
    var myId: String = ""
    var nickName: MutableLiveData<String> = MutableLiveData()
    var updateConfirm: MutableLiveData<Boolean> = MutableLiveData()

    fun setMyId(email: String): String {
        myId = email
        return myId
    }

    fun selectMyNickName(): MutableLiveData<String> {
        userRef.child(replacePointToComma(myId)).child("nickName")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nickName.value = snapshot.value.toString()
                }
                override fun onCancelled(error: DatabaseError) {}

            })
        return nickName
    }

    fun updateNickName(name: String) {
        userRef.child(replacePointToComma(myId)).child("nickName").setValue(name)
        updateConfirm.value = true
    }

    fun resetConfirm(): MutableLiveData<Boolean> {
        return updateConfirm
    }
}
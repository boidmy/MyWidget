package com.mywidget.ui.mypage

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.ChatInviteModel
import com.mywidget.data.model.RoomDataModel
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

class MyPageRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    @Inject @Named("Room") lateinit var roomRef: DatabaseReference
    @Inject @Named("User")
    lateinit var userRef: DatabaseReference
    var myId: String = ""
    var nickName: MutableLiveData<String> = MutableLiveData()
    var updateConfirm: MutableLiveData<Boolean> = MutableLiveData()
    var roomList = arrayListOf<RoomDataModel>()

    fun setMyId(email: String): String {
        myId = email
        return myId
    }

    fun selectMyNickName(): MutableLiveData<String> {
        userRef.child(replacePointToComma(myId))
            .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nickName.value = snapshot.child("nickName").value.toString()
                for (chatRoom: DataSnapshot in snapshot.child("RoomList").children) {
                    val roomData = chatRoom.getValue(RoomDataModel::class.java)
                    roomData?.let {
                        roomList.add(it)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return nickName
    }

    fun updateNickName(name: String) {
        userRef.child(replacePointToComma(myId)).child("nickName").setValue(name)
        updateChatNickName(name)

        updateConfirm.value = true
    }

    private fun updateChatNickName(name: String) {
        for (roomData in roomList) {
            roomRef.child(roomData.master).child(roomData.roomKey).child("invite")
                .child(replacePointToComma(myId)).setValue(ChatInviteModel(name, true))
        }
    }

    fun resetConfirm(): MutableLiveData<Boolean> {
        return updateConfirm
    }
}
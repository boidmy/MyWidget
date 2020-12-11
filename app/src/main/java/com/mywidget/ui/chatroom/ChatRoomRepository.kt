package com.mywidget.ui.chatroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.RoomDataModel
import util.Util.replacePointToComma
import javax.inject.Inject

class ChatRoomRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()
    private val ROOMLIST = "RoomList"

    fun selectRoomList(id: String): MutableLiveData<List<RoomDataModel>> {
        userRef.child(replacePointToComma(id))
            .child(ROOMLIST).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<RoomDataModel> = arrayListOf()
                for (snap: DataSnapshot in snapshot.children) {
                    val roomModel = RoomDataModel(
                        snap.child("roomName").value.toString(),
                        snap.child("roomKey").value.toString(),
                        snap.child("master").value.toString()
                    )
                    list.add(roomModel)
                }
                roomList.value = list
            }
        })
        return roomList
    }

    fun createRoom(id: String, subject: String) {
        id.let {
            val mEmail = replacePointToComma(id)

            val result: HashMap<String, String> = hashMapOf()
            result["roomName"] = subject
            val ref = roomRef.child(mEmail).push()
            ref.setValue(result)
            roomMasterTokenSave(id, ref)
            ref.key?.let { keyVal ->
                addUserRoomInformation(RoomDataModel(subject, keyVal, mEmail))
            }
        }
    }

    private fun roomMasterTokenSave(email: String, ref: DatabaseReference) {
        userRef.child(replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val userEmail: String = snapshot.child("email").value.toString()
                        val userToken: String = snapshot.child("token").value.toString()
                        ref.child("invite").child(replacePointToComma(userEmail))
                            .setValue(userToken)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun addUserRoomInformation(roomDataModel: RoomDataModel) {
        userRef.child(roomDataModel.master)
            .child(ROOMLIST).child(roomDataModel.roomKey).setValue(roomDataModel)
    }

    fun deleteRoom(master: String, roomKey: String, myId: String) {
        userRef.child(replacePointToComma(myId))
            .child(ROOMLIST).child(roomKey).setValue(null)
        roomRef.child(replacePointToComma(master)).child(roomKey)
            .child("invite").child(replacePointToComma(myId)).setValue(null)
    }
}
package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.RoomDataModel
import util.Util
import javax.inject.Inject

class ChatRoomRepository @Inject constructor() {
    @Inject
    lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()

    fun selectRoomList(id: String): MutableLiveData<List<RoomDataModel>> {
        userRef.child(Util.replacePointToComma(id)).child("RoomList").addValueEventListener(object : ValueEventListener {
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
            val mEmail = Util.replacePointToComma(id)

            val result: HashMap<String, String> = hashMapOf()
            result["roomName"] = subject
            val ref = roomRef.child(mEmail).push()
            ref.setValue(result)
            ref.key?.let { keyVal ->
                addUserRoomInformation(RoomDataModel(subject, keyVal, mEmail))
            }
        }
    }

    private fun addUserRoomInformation(roomDataModel: RoomDataModel) {
        userRef.child(roomDataModel.master).child("RoomList").push().setValue(roomDataModel)
    }
}
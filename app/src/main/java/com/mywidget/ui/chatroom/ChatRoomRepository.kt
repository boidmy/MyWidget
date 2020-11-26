package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatRoomRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy {database.child("Room")}
    private val userRef: DatabaseReference by lazy {database.child("User")}
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()

    fun selectRoomList(id: String) : MutableLiveData<List<RoomDataModel>> {
        userRef.child(id).child("RoomList").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<RoomDataModel> = arrayListOf()
                for(snap: DataSnapshot in snapshot.children) {
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

    fun createRoom(id: String) {
        id.let {
            val mEmail = it.substring(0, it.indexOf('@'))
            val result: HashMap<String, String> = hashMapOf()
            val roomName = "콩이네방"
            result["roomName"] = roomName

            val ref = roomRef.child(mEmail).push()
            ref.setValue(result)
            ref.key?.let { keyVal ->
                addUserRoomInformation(keyVal, mEmail, roomName)
            }
        }
    }

    private fun addUserRoomInformation(key: String, id: String, roomName: String) {
        val hopperUpdates: HashMap<String, Any> = hashMapOf()
        hopperUpdates["roomKey"] = key
        hopperUpdates["master"] = id //마스터 아디 수정해야함
        hopperUpdates["roomName"] = roomName
        userRef.child(id).child("RoomList").push().setValue(hopperUpdates)
    }
}
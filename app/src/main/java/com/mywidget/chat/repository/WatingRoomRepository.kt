package com.mywidget.chat.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.chat.RoomDataModel
import javax.inject.Inject

class WatingRoomRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy {database.child("Room")}
    private val userRef: DatabaseReference by lazy {database.child("User")}
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()

    fun selectRoomList(id: String) : MutableLiveData<List<RoomDataModel>> {
        roomRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<RoomDataModel> = arrayListOf()
                for(snap: DataSnapshot in snapshot.children) {
                    val roomModel = RoomDataModel()
                    roomModel.roomName = snap.child("roomName").value as String?
                    roomModel.key = snap.key
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
            result["roomName"] = "콩이네방"

            val ref = roomRef.child(mEmail).push()
            ref.setValue(result)
            ref.key?.let { keyVal ->
                addUserRoomInformation(keyVal, mEmail)
            }
        }
    }

    private fun addUserRoomInformation(key: String, id: String) {
        val hopperUpdates: HashMap<String, Any> = hashMapOf()
        hopperUpdates["room"] = key
        hopperUpdates["master"] = id //마스터 아디 수정해야함
        userRef.child(id).child("RoomList").push().setValue(hopperUpdates)
    }
}
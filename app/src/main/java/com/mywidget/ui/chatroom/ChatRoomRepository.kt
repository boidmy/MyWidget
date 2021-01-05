package com.mywidget.ui.chatroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.*
import util.Util.replacePointToComma
import javax.inject.Inject

class ChatRoomRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()
    private val ROOMLIST = "RoomList"
    private val friendMap = hashMapOf<String, String>()
    var friendHashMap: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var roomLastMessage: MutableLiveData<List<ChatDataModel>> = MutableLiveData()

    fun selectRoomList(id: String): MutableLiveData<List<RoomDataModel>> {
        userRef.child(replacePointToComma(id))
            .child(ROOMLIST).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<RoomDataModel> = arrayListOf()
                for (snap: DataSnapshot in snapshot.children) {
                    val room = snap.getValue(RoomDataModel::class.java)
                    room?.let {
                        list.add(it)
                    }
                }
                roomList.value = list
            }
            override fun onCancelled(error: DatabaseError) {}

        })
        return roomList
    }

    fun selectLastMessage(data: List<RoomDataModel>) {
        val dataList = arrayListOf<ChatDataModel>()
        for (i in data.indices) {
            dataList.add(ChatDataModel())
        }
        for ((index, input) in data.withIndex()) {
            roomRef.child(replacePointToComma(input.master)).child(input.roomKey)
                .child("message").limitToLast(1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            val key = snap.key
                            key?.let {
                                input.lastMessage = snapshot.child(it).child("message").value as String
                            }
                        }

                        dataList[index] = ChatDataModel(input.lastMessage)
                        roomLastMessage.value = dataList
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    fun resetLastMessage(): MutableLiveData<List<ChatDataModel>>{
        return roomLastMessage
    }

    fun selectFriendList(id: String): MutableLiveData<HashMap<String, String>> {
        userRef.child(replacePointToComma(id)).child("friend")
            .child("friendList").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (friend in snapshot.children) {
                        val friendModel = friend.getValue(FriendModel::class.java)
                        friendModel?.let {
                            if (it.email.isNotEmpty()) {
                                friendMap[it.email] = it.nickName
                            }
                        }
                    }
                    friendHashMap.value = friendMap
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        return friendHashMap
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
        val mEmail = replacePointToComma(email)
        userRef.child(mEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserData::class.java)
                    user?.let {
                        ref.child("invite").child(mEmail)
                            .setValue(ChatInviteModel(it.nickName, true))
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
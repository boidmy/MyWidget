package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.ChatInviteModel
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.data.model.UserData
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
package com.mywidget.ui.chatinvite

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.ChatInviteModel
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.data.model.UserData
import util.Util
import util.Util.replaceCommaToPoint
import util.Util.replacePointToComma
import javax.inject.Inject

class ChatInviteRepository @Inject constructor() {
    @Inject
    lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val roomRef: DatabaseReference by lazy {
        database.child("Room").child(roomDataModel.master).child(roomDataModel.roomKey) }
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()
    private lateinit var roomDataModel: RoomDataModel

    fun setChatRoomInformation(roomDataModel: RoomDataModel) {
        this.roomDataModel = roomDataModel
    }

    fun selectFriendList(myId: String): MutableLiveData<ArrayList<FriendModel>> {
        userRef.child(replacePointToComma(myId)).child("friend")
            .child("friendList").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val array = arrayListOf<FriendModel>()
                    for (snap: DataSnapshot in snapshot.children) {
                        val friendModel = snap.getValue(FriendModel::class.java)
                        friendModel?.let {
                            array.add(friendModel)
                        }
                    }
                    friendList.value = array
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        return friendList
    }

    fun inviteUser(email: String) {
        val mEmail = replacePointToComma(email)
        userRef.child(replacePointToComma(mEmail))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserData::class.java)
                    user?.let {
                        userRef.child(mEmail).child("RoomList").child(roomDataModel.roomKey)
                            .setValue(roomDataModel)
                        roomRef.child("invite")
                            .child(mEmail)
                            .setValue(ChatInviteModel(it.nickName, true))
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
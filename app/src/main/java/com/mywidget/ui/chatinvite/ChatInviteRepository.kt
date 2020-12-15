package com.mywidget.ui.chatinvite

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import util.Util
import javax.inject.Inject

class ChatInviteRepository @Inject constructor() {
    @Inject
    lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val roomRef: DatabaseReference by lazy {
        database.child("Room").child(roomDataModel.master).child(roomDataModel.roomKey) }
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()
    private val inviteUserExistence: MutableLiveData<Boolean> = MutableLiveData()
    private val inviteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var roomDataModel: RoomDataModel

    fun setChatRoomInformation(roomDataModel: RoomDataModel) {
        this.roomDataModel = roomDataModel
    }

    fun selectFriendList(myId: String): MutableLiveData<ArrayList<FriendModel>> {
        userRef.child(Util.replacePointToComma(myId)).child("friend")
            .child("friendList").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val array = arrayListOf<FriendModel>()
                    for (snap: DataSnapshot in snapshot.children) {
                        val email = snap.key.toString()
                        val explanation = snap.value.toString()
                        array.add(FriendModel(Util.replaceCommaToPoint(email), explanation))
                    }
                    friendList.value = array
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        return friendList
    }

    fun inviteUserExistence(): MutableLiveData<Boolean> {
        return inviteUserExistence
    }

    fun inviteDialogVisibility(): MutableLiveData<Boolean> {
        return inviteDialogVisibility
    }

    fun inviteUser(email: String) {
        val mEmail = Util.replacePointToComma(email)
        userRef.child(mEmail).child("RoomList").child(roomDataModel.roomKey)
            .setValue(roomDataModel)
        roomRef.child("invite")
            .child(Util.replacePointToComma(email)).setValue("user")
    }

    fun inviteDialogShow(flag: Boolean) {
        inviteDialogVisibility.value = flag
    }

    fun userExistenceChk(email: String) {
        userRef.child(Util.replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val userEmail: String = snapshot.child("email").value.toString()
                        val userToken: String = snapshot.child("token").value.toString()

                        roomRef.child("invite")
                            .child(Util.replacePointToComma(userEmail)).setValue(userToken)
                        inviteUserExistence.value = true
                    } else {
                        inviteUserExistence.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
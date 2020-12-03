package com.mywidget.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.di.custom.ActivityScope
import util.Util
import javax.inject.Inject

@ActivityScope
class ChatRepository @Inject constructor() {

    @Inject
    lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val message: DatabaseReference by lazy {
        roomRef.child(roomDataModel.master).child(roomDataModel.key).child("message")
    }

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    val list: ArrayList<ChatDataModel> = arrayListOf()
    private lateinit var roomDataModel: RoomDataModel

    private var limitCount = 20
    fun selectChat(roomDataModel: RoomDataModel): MutableLiveData<List<ChatDataModel>> {
        this.roomDataModel = roomDataModel
        message.limitToLast(limitCount).addValueEventListener(itemSelectListener)
        limitCount = 1
        return data
    }

    fun insertChat(sendUserEmail: String, text: String) {
        val userEmail = Util.replacePointToComma(sendUserEmail)
        message.push().setValue(ChatDataModel(text, userEmail))
    }

    fun inviteUser(email: String) {
        val mEmail = Util.replacePointToComma(email)
        userRef.child(mEmail).child("RoomList").push().setValue(roomDataModel)
    }

    private val itemSelectListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            for (snap: DataSnapshot in snapshot.children) {
                val chatModel = ChatDataModel(
                    snap.child("message").value.toString(),
                    snap.child("id").value.toString()
                )
                list.add(0, chatModel)
            }
            data.value = list
        }
    }
}
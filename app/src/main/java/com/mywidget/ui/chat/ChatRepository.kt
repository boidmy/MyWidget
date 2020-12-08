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

    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val message: DatabaseReference by lazy {
        roomRef.child(roomDataModel.master).child(roomDataModel.roomKey).child("message")
    }
    private var inviteUserExistence: MutableLiveData<Boolean> = MutableLiveData()
    private var inviteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    val list: ArrayList<ChatDataModel> = arrayListOf()
    var lastMessageKey: String? = null
    var loadFirstMessage = false
    private lateinit var roomDataModel: RoomDataModel

    fun getListChat(roomDataModel: RoomDataModel): MutableLiveData<List<ChatDataModel>> {
        this.roomDataModel = roomDataModel
        message.limitToLast(20).addChildEventListener(itemSelectListener)
        return data
    }

    fun insertChat(sendUserEmail: String, text: String) {
        val userEmail = Util.replacePointToComma(sendUserEmail)
        message.push().setValue(ChatDataModel(text, userEmail))
    }

    fun userExistenceChk(email: String) {
        userRef.child(Util.replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                inviteUserExistence.value = snapshot.value != null
            }
        })
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
        inviteDialogShow(false)
    }

    fun inviteDialogShow(flag: Boolean) {
        inviteDialogVisibility.value = flag
    }

    private val itemSelectListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d("","")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("","")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("","")
        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val i = snapshot.children.iterator()
            var id = ""
            var message = ""
            while (i.hasNext()) {
                id = i.next().value as String
                message = i.next().value as String
            }
            list.add(0, ChatDataModel(message, id))
            data.value = list
            if(!loadFirstMessage) {
                lastMessageKey = snapshot.key
                loadFirstMessage = true
            }
        }
        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("","")
        }
    }

    fun chatLoadMore(startPosition: Int) {
        loadFirstMessage = false
        val loadMoreSelectKey = lastMessageKey
        message.orderByKey().endAt(lastMessageKey).limitToLast(20)
            .addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val i = snapshot.children.iterator()
                var id = ""
                var message = ""
                while (i.hasNext()) {
                    id = i.next().value as String
                    message = i.next().value as String
                }
                if(loadMoreSelectKey != snapshot.key) {
                    list.add(startPosition, ChatDataModel(message, id))
                    data.value = list
                }
                if(!loadFirstMessage) {
                    lastMessageKey = snapshot.key
                    loadFirstMessage = true
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }
}
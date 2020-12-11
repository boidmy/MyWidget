package com.mywidget.ui.chat

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mywidget.SendPush
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.di.custom.ActivityScope
import util.Util.replaceCommaToPoint
import util.Util.replacePointToComma
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

@ActivityScope
class ChatRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy {
        database.child("Room").child(roomDataModel.master).child(roomDataModel.roomKey) }
    private val userRef: DatabaseReference by lazy { database.child("User") }
    private val message: DatabaseReference by lazy { roomRef.child("message") }
    private val inviteUserExistence: MutableLiveData<Boolean> = MutableLiveData()
    private val inviteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val inviteUserList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    var myId: String? = null
    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    val list: ArrayList<ChatDataModel> = arrayListOf()
    var lastMessageKey: String? = null
    var loadMoreChk = false
    private lateinit var roomDataModel: RoomDataModel
    private var isListening = true

    fun userId(userEmail: String): String {
        myId = replacePointToComma(userEmail)
        return myId ?: ""
    }

    fun getListChat(roomDataModel: RoomDataModel): MutableLiveData<List<ChatDataModel>> {
        this.roomDataModel = roomDataModel
        message.limitToLast(20).addChildEventListener(itemSelectListener)
        return data
    }

    fun insertChat(sendUserEmail: String, text: String) {
        val userEmail = replacePointToComma(sendUserEmail)
        val ref = message.push()
        ref.setValue(ChatDataModel(text, userEmail)).addOnCompleteListener {
            Handler(Looper.getMainLooper()).postDelayed({
                insertPush(ref.key ?: "", text, userEmail)
            }, 1000)
        }
    }

    private val itemSelectListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d("", "")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("", "")
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("", "")
        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            if (!isListening) return
            val i = snapshot.children.iterator()
            var id = ""
            var message = ""
            while (i.hasNext()) {
                id = i.next().value as String
                message = i.next().value as String
            }
            list.add(0, ChatDataModel(message, id))
            data.value = list
            if (!loadMoreChk) {
                lastMessageKey = snapshot.key
                loadMoreChk = true
            }
            //메시지를 보고있는 사람은 자기의 데이터에 key값이 여기서 들어감
            roomRef.child("invite").child(myId ?: "").setValue(snapshot.key)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d("", "")
        }
    }

    fun chatLoadMore(startPosition: Int) {
        loadMoreChk = false
        val loadMoreSelectKey = lastMessageKey
        message.orderByKey().endAt(lastMessageKey).limitToLast(20)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val i = snapshot.children.iterator()
                    var id = ""
                    var message = ""
                    while (i.hasNext()) {
                        id = i.next().value as String
                        message = i.next().value as String
                    }
                    if (loadMoreSelectKey != snapshot.key) {
                        list.add(startPosition, ChatDataModel(message, id))
                        data.value = list
                    }
                    if (!loadMoreChk) {
                        lastMessageKey = snapshot.key
                        loadMoreChk = true
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {}
            })
    }

    fun inviteUserList(): MutableLiveData<ArrayList<String>> {
        roomRef.child("invite").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<String> = arrayListOf()
                for (snap: DataSnapshot in snapshot.children) {
                    list.add(replaceCommaToPoint(snap.key.toString()))
                }
                inviteUserList.value = list
            }
        })
        return inviteUserList
    }

    fun userExistenceChk(email: String) {
        userRef.child(replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val userEmail: String = snapshot.child("email").value.toString()
                        val userToken: String = snapshot.child("token").value.toString()

                        roomRef.child("invite")
                            .child(replacePointToComma(userEmail)).setValue(userToken)
                        inviteUserExistence.value = true
                    } else {
                        inviteUserExistence.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun inviteUserExistence(): MutableLiveData<Boolean> {
        return inviteUserExistence
    }

    fun inviteDialogVisibility(): MutableLiveData<Boolean> {
        return inviteDialogVisibility
    }

    fun inviteUser(email: String) {
        val mEmail = replacePointToComma(email)
        userRef.child(mEmail).child("RoomList").child(roomDataModel.roomKey)
            .setValue(roomDataModel)
        inviteDialogShow(false)
    }

    fun inviteDialogShow(flag: Boolean) {
        inviteDialogVisibility.value = flag
    }

    private fun insertPush(key: String, message: String, sendId: String) {
        roomRef.child("invite").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap: DataSnapshot in snapshot.children) {
                        if (snap.value != key) {
                            //유저의 토큰값을 검색해서 푸쉬를 날려줘야함
                            getUserTokenAndPush(snap.key ?: "", message, sendId)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    fun getUserTokenAndPush(email: String, message: String, sendId: String) {
        userRef.child(email).child("token").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.value?.let {
                        SendPush().send(it.toString(), message, sendId)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    fun onCleared() {
        isListening = false
    }
}
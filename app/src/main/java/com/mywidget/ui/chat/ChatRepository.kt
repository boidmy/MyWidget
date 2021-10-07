package com.mywidget.ui.chat

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mywidget.data.model.*
import com.mywidget.fcm.SendPush
import com.mywidget.di.custom.ActivityScope
import util.CalendarUtil
import util.CalendarUtil.yearDateFormat
import util.ChildEventListenerUse
import util.Util.replaceCommaToPoint
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

@ActivityScope
class ChatRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    @Inject @Named("User") lateinit var userRef: DatabaseReference
    private val roomRef: DatabaseReference by lazy {
        database.child("Room").child(roomDataModel.master).child(roomDataModel.roomKey)
    }
    private val message: DatabaseReference by lazy { roomRef.child("message") }
    private val inviteUserList: MutableLiveData<List<ChatInviteModel>> = MutableLiveData()
    var myId: String? = null
    var data: MutableLiveData<List<ChatData>> = MutableLiveData()
    val list: MutableList<ChatData> = mutableListOf()
    var lastMessageKey: String? = null
    var loadMoreChk = false
    private lateinit var roomDataModel: RoomDataModel
    private var isListening = true
    var inviteDatabaseMap = HashMap<String, String>()

    fun setRoomData(roomDataModel: RoomDataModel) {
        this.roomDataModel = roomDataModel
    }

    fun setInviteDatabaseMap(): HashMap<String, String> {
        return inviteDatabaseMap
    }

    fun userId(userEmail: String): String {
        myId = replacePointToComma(userEmail)
        return myId ?: ""
    }

    fun getListChat(): MutableLiveData<List<ChatData>> {
        message.limitToLast(20).addChildEventListener(itemSelectListener)
        return data
    }

    fun insertChat(sendUserEmail: String, text: String) {
        val userEmail = replacePointToComma(sendUserEmail)
        val ref = message.push()
        ref.setValue(ChatDataModel(text, userEmail, CalendarUtil.getDate()))
            .addOnCompleteListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    insertPush(ref.key ?: "", text, userEmail)
                }, 2000)
            }
    }

    private val itemSelectListener = object : ChildEventListenerUse {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            if (!isListening) return
            val chatData = chatData(snapshot.children.iterator(), previousChildName)
            list.add(0, chatData)
            data.value = list
            if (!loadMoreChk) {
                lastMessageKey = snapshot.key
                loadMoreChk = true
            }
            //메시지를 보고있는 사람은 자기의 데이터에 key값이 여기서 들어감
            //roomRef.child("invite").child(myId ?: "").setValue(snapshot.key)
        }
    }

    fun chatLoadMore(startPosition: Int) {
        loadMoreChk = false
        val loadMoreSelectKey = lastMessageKey
        message.orderByKey().endAt(lastMessageKey).limitToLast(20)
            .addChildEventListener(object : ChildEventListenerUse {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val chatData = chatData(snapshot.children.iterator(), previousChildName)
                    if (loadMoreSelectKey != snapshot.key) {
                        list.add(startPosition, chatData)
                        data.value = list
                    }
                    if (!loadMoreChk) {
                        lastMessageKey = snapshot.key
                        loadMoreChk = true
                    }
                }
            })
    }

    fun chatData(iterator: MutableIterator<DataSnapshot>, key: String?): ChatData {
        val chatData = ChatData()
        while (iterator.hasNext()) {
            chatData.chatDataModel.apply {
                id = iterator.next().value as String
                message = iterator.next().value as String
                nickName = iterator.next().value as String
                time = yearDateFormat(iterator.next().value as String)
            }
        }
        chatData.key = key ?: ""
        return chatData
    }

    fun inviteUserList(): MutableLiveData<List<ChatInviteModel>> {
        roomRef.child("invite").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<ChatInviteModel> = mutableListOf()
                for (snap: DataSnapshot in snapshot.children) {
                    val user = snap.getValue(ChatInviteModel::class.java)
                    user?.let {
                        it.email = replaceCommaToPoint(snap.key.toString())
                        if (it.inviteFlag) {
                            list.add(user)
                        }
                        inviteDatabaseMap.put(snap.key.toString(), user.nickName)
                    }
                }
                inviteUserList.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return inviteUserList
    }

    private fun insertPush(key: String, message: String, sendId: String) {
        roomRef.child("invite").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap: DataSnapshot in snapshot.children) {
                        val data = snap.getValue(ChatInviteModel::class.java)
                        if (sendId != snap.key && data?.inviteFlag == true) {
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
        userRef.child(email).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val token = snapshot.child("token").value
                    val friendData = snapshot.child("friendList")
                        .child(replacePointToComma(sendId)).getValue(FriendModel::class.java)
                    val friendMyNickName = friendData?.nickName ?: ""
                    var sendMyNickName = sendId
                    if (friendMyNickName.isNotEmpty()) sendMyNickName = friendMyNickName
                    token?.let {
                        SendPush().send(it.toString(), message, sendMyNickName, roomDataModel)
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
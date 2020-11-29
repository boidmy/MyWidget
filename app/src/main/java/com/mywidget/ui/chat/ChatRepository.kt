package com.mywidget.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.mywidget.data.model.ChatDataModel
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    val list: ArrayList<ChatDataModel> = arrayListOf()
    private var master: String = ""
    private var roomKey: String = ""
    private val message: DatabaseReference by lazy {
        roomRef.child(master).child(roomKey).child("message") }
    fun selectChat(id: String, key: String): MutableLiveData<List<ChatDataModel>> {
        master = id
        roomKey = key
        changeCatch()
        message.addListenerForSingleValueEvent(object : ValueEventListener {
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
            })
        return data
    }

    private fun changeCatch() {
        message.limitToLast(1).addValueEventListener(object : ValueEventListener {
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
            })
    }

    fun insertChat(sendUserEmail: String, text: String) {
        val userEmail = userId(sendUserEmail)
        val result: HashMap<String, String> = hashMapOf()
        result["message"] = text
        result["id"] = userEmail
        message.push().setValue(result)
    }

    fun userId(userEmail: String): String {
        val mEmail: List<String>? = userEmail.split("@")
        var userId = ""
        if (mEmail?.size ?: 0 > 0) {
            userId = mEmail?.get(0) ?: ""
        }
        return userId
    }

    fun inviteUser() {

    }
}
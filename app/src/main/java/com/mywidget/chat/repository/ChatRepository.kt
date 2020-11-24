package com.mywidget.chat.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.chat.ChatDataModel
import com.mywidget.chat.RoomDataModel
import kotlinx.android.synthetic.main.activity_chatting.*
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    @Inject
    lateinit var database: DatabaseReference
    private val roomRef: DatabaseReference by lazy { database.child("Room") }
    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()

    fun selectChat(id: String, key: String): MutableLiveData<List<ChatDataModel>> {
        roomRef.child(id).child(key).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list: ArrayList<ChatDataModel> = arrayListOf()
                    for (snap: DataSnapshot in snapshot.children) {
                        val chatModel = ChatDataModel()
                        chatModel.message = snap.child("message").value as String?
                        chatModel.id = snap.child("id").value as String?
                        list.add(chatModel)
                    }
                    data.value = list
                }
            })
        return data
    }

    fun insertChat(master: String, roomKey: String, sendUserEmail: String, text: String) {
        val userEmail = userId(sendUserEmail)
        val result: HashMap<String, String> = hashMapOf()
        result["message"] = text
        result["id"] = userEmail
        roomRef.child(master).child(roomKey)
            .child("message").push().setValue(result)
    }

    fun userId(userEmail: String): String {
        val mEmail: List<String>? = userEmail.split("@")
        var userEmail = ""
        if (mEmail?.size ?: 0 > 0) {
            userEmail = mEmail?.get(0) ?: ""
        }
        return userEmail
    }
}
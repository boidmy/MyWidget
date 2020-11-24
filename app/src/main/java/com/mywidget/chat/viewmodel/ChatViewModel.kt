package com.mywidget.chat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.chat.ChatDataModel
import com.mywidget.chat.repository.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    var myId: String? = null

    fun selectChat(id: String, key: String) {
        data = repository.selectChat(id, key)
    }

    fun insertChat(master: String, roomKey: String, sendUserEmail: String, text: String) {
        repository.insertChat(master, roomKey, sendUserEmail, text)
    }

    fun userId(userEmail: String) {
        myId = repository.userId(userEmail)
    }

    fun inviteUser() {

    }
}
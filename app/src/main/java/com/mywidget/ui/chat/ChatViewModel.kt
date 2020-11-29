package com.mywidget.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.ChatDataModel
import com.mywidget.ui.chat.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    var myId: String? = null

    fun selectChat(id: String, key: String) {
        data = repository.selectChat(id, key)
    }

    fun insertChat(sendUserEmail: String, text: String) {
        repository.insertChat(sendUserEmail, text)
    }

    fun userId(userEmail: String) {
        myId = repository.userId(userEmail)
    }

    fun inviteUser() {

    }
}
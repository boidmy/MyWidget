package com.mywidget.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.ui.chat.ChatRepository
import util.Util
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    var myId: String? = null

    fun getListChat(roomDataModel: RoomDataModel) {
        data = repository.getListChat(roomDataModel)
    }

    fun insertChat(sendUserEmail: String, text: String) {
        repository.insertChat(sendUserEmail, text)
    }

    fun userId(userEmail: String) {
        myId = Util.replacePointToComma(userEmail)
    }

    fun inviteUser(email: String) {
        repository.inviteUser(email)
    }

    fun chatLoadMore(startPosition: Int) {
        repository.chatLoadMore(startPosition)
    }
}
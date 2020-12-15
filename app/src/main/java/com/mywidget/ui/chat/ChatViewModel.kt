package com.mywidget.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.di.custom.ActivityScope
import javax.inject.Inject

@ActivityScope
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    var myId: String? = null
    var inviteUserList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    fun getListChat(roomDataModel: RoomDataModel) {
        data = repository.getListChat(roomDataModel)
    }

    fun insertChat(sendUserEmail: String, text: String) {
        repository.insertChat(sendUserEmail, text)
    }

    fun myId(userEmail: String) {
        myId = repository.userId(userEmail)
    }

    fun inviteUserList() {
        inviteUserList = repository.inviteUserList()
    }

    fun chatLoadMore(startPosition: Int) {
        repository.chatLoadMore(startPosition)
    }

    override fun onCleared() {
        super.onCleared()
        repository.onCleared()
    }
}
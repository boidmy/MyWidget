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
    var inviteUserExistence: MutableLiveData<Boolean> = MutableLiveData()
    var inviteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var myId: String? = null
    var inviteUserList: MutableLiveData<ArrayList<String>> = MutableLiveData()

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

    fun inviteUserList() {
        inviteUserList = repository.inviteUserList()
    }

    fun userExistenceChk(email: String) {
        repository.userExistenceChk(email)
    }

    fun chatLoadMore(startPosition: Int) {
        repository.chatLoadMore(startPosition)
    }

    fun inviteUserExistence() {
        inviteUserExistence = repository.inviteUserExistence()
    }

    fun inviteDialogVisibility() {
        inviteDialogVisibility = repository.inviteDialogVisibility()
    }

    fun inviteDialogShow() {
        repository.inviteDialogShow(true)
    }
}
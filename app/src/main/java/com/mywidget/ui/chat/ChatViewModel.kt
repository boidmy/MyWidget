package com.mywidget.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.ChatInviteModel
import com.mywidget.data.model.RoomDataModel
import com.mywidget.di.custom.ActivityScope
import javax.inject.Inject

@ActivityScope
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    var data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    var inviteUserList: MutableLiveData<ArrayList<ChatInviteModel>> = MutableLiveData()
    var myId: String = ""
    var inviteDatabaseMap = HashMap<String, String>()
    lateinit var friendHashMap: HashMap<String, String>

    fun setFriendHashMap(hashData: Map<String, String>) {
        friendHashMap = hashData as HashMap<String, String>
    }

    fun setRoomData(roomDataModel: RoomDataModel) {
        repository.setRoomData(roomDataModel)
    }

    fun setInviteDatabaseMap() {
        inviteDatabaseMap = repository.setInviteDatabaseMap()
    }

    fun getListChat() {
        data = repository.getListChat()
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
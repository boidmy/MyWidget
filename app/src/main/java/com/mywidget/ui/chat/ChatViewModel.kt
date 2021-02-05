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

    private var _data: MutableLiveData<List<ChatDataModel>> = MutableLiveData()
    private var _inviteUserList: MutableLiveData<MutableList<ChatInviteModel>> = MutableLiveData()
    private var _myId: String = ""
    private var _inviteDatabaseMap = HashMap<String, String>()
    private var _friendHashMap: HashMap<String, String> = hashMapOf()

    val data: MutableLiveData<List<ChatDataModel>>
        get() = _data

    val inviteUserList: MutableLiveData<MutableList<ChatInviteModel>>
        get() = _inviteUserList

    val inviteDatabaseMap: HashMap<String, String>
        get() = _inviteDatabaseMap

    val myId: String
        get() = _myId

    val friendHashMap: HashMap<String, String>
        get() = _friendHashMap

    fun setFriendHashMap(hashData: Map<String, String>) {
        _friendHashMap = hashData as HashMap<String, String>
    }

    fun setRoomData(roomDataModel: RoomDataModel) {
        repository.setRoomData(roomDataModel)
    }

    fun setInviteDatabaseMap() {
        _inviteDatabaseMap = repository.setInviteDatabaseMap()
    }

    fun getListChat() {
        _data = repository.getListChat()
    }

    fun insertChat(sendUserEmail: String, text: String) {
        repository.insertChat(sendUserEmail, text)
    }

    fun myId(userEmail: String) {
        _myId = repository.userId(userEmail)
    }

    fun inviteUserList() {
        _inviteUserList = repository.inviteUserList()
    }

    fun chatLoadMore(startPosition: Int) {
        repository.chatLoadMore(startPosition)
    }

    override fun onCleared() {
        super.onCleared()
        repository.onCleared()
    }
}
package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.ChatDataModel
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository
) : ViewModel() {
    private var _roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()
    var myId: String? = null
    private val _isDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var _friendHashMap: MutableLiveData<Map<String, String>> = MutableLiveData()
    private val _deleteRoom: MutableLiveData<RoomDataModel> = MutableLiveData()
    private val _deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _enterRoom: MutableLiveData<RoomDataModel> = MutableLiveData()
    private var _roomLastMessage: MutableLiveData<List<ChatDataModel>> = MutableLiveData()

    val roomList: MutableLiveData<List<RoomDataModel>>
        get() = _roomList

    val isDialogVisibility: MutableLiveData<Boolean>
        get() = _isDialogVisibility

    val friendHashMap: MutableLiveData<Map<String, String>>
        get() = _friendHashMap

    val deleteRoom: MutableLiveData<RoomDataModel>
        get() = _deleteRoom

    val deleteDialogVisibility: MutableLiveData<Boolean>
        get() = _deleteDialogVisibility

    val enterRoom: MutableLiveData<RoomDataModel>
        get() = _enterRoom

    val roomLastMessage: MutableLiveData<List<ChatDataModel>>
        get() = _roomLastMessage

    fun selectRoomList(id: String) {
        _roomList = repository.selectRoomList(id)
    }

    fun selectFriendList(id: String) {
        _friendHashMap = repository.selectFriendList(id)
    }

    fun resetLastMessage() {
        _roomLastMessage = repository.resetLastMessage()
    }

    fun selectLastMessage(data: List<RoomDataModel>) {
        repository.selectLastMessage(data)
    }

    fun createRoom(id: String, subject: String) {
        repository.createRoom(id, subject)
    }

    fun deleteRoom(data: RoomDataModel) {
        repository.deleteRoom(data.master, data.roomKey, myId?:"")
        deleteDialogVisibility(false)
    }

    fun dialogVisibility(flag: Boolean) {
        isDialogVisibility.value = flag
    }

    fun deleteDialogVisibility(flag: Boolean) {
        deleteDialogVisibility.value = flag
    }

    fun enterRoom(roomData: RoomDataModel) {
        enterRoom.value = roomData
    }

    fun setDeleteData(data: RoomDataModel) {
        deleteRoom.value = data
    }
}
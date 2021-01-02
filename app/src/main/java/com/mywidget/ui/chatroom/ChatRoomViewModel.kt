package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository
) : ViewModel() {
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()
    var myId: String? = null
    var isDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    //var friendHashMap = hashMapOf<String, String>()
    var friendHashMap: MutableLiveData<HashMap<String, String>> = MutableLiveData()

    var deleteRoom: MutableLiveData<RoomDataModel> = MutableLiveData()
    var deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val enterRoom: MutableLiveData<RoomDataModel> = MutableLiveData()

    fun selectRoomList(id: String) {
        roomList = repository.selectRoomList(id)
    }

    fun selectFriendList(id: String) {
        friendHashMap = repository.selectFriendList(id)
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
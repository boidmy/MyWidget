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

    var deleteRoom: MutableLiveData<RoomDataModel> = MutableLiveData()
    var deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    fun selectRoomList(id: String) {
        roomList = repository.selectRoomList(id)
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
}
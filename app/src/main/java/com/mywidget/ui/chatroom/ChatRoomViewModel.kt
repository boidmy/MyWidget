package com.mywidget.ui.chatroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository
) : ViewModel() {
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()

    fun selectRoomList(id: String) {
        roomList = repository.selectRoomList(id)
    }

    fun createRoom(id: String) {
        repository.createRoom(id)
    }
}
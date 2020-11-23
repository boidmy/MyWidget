package com.mywidget.chat.waiting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.chat.RoomDataModel
import com.mywidget.chat.waiting.repository.WatingRoomRepository
import javax.inject.Inject

class WatingRoomViewModel @Inject constructor(
    val repository: WatingRoomRepository
) : ViewModel() {
    var roomList: MutableLiveData<List<RoomDataModel>> = MutableLiveData()

    fun selectRoomList(id: String) {
        roomList = repository.selectRoomList(id)
    }

    fun createRoom(id: String) {
        repository.createRoom(id)
    }
}
package com.mywidget.chat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.chat.RoomDataModel
import com.mywidget.chat.repository.WatingRoomRepository
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
package com.mywidget.ui.chatinvite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatInviteViewModel @Inject constructor(
    private val repository: ChatInviteRepository) : ViewModel() {

    private var _myId: String = ""
    private var _friendList: MutableLiveData<MutableList<FriendModel>> = MutableLiveData()

    val myId: String
        get() = _myId

    val friendList: MutableLiveData<MutableList<FriendModel>>
        get() = _friendList

    fun setChatRoomInformation(roomDataModel: RoomDataModel) {
        repository.setChatRoomInformation(roomDataModel)
    }

    fun selectFriendList() {
        _friendList = repository.selectFriendList(myId)
    }

    fun myId(userEmail: String) {
        _myId = userEmail
    }

    private fun inviteUser(email: String) {
        repository.inviteUser(email)
    }

    fun inviteUserArray() {
        friendList.value?.let {
            for(ar in it){
                if(ar.selector) {
                    inviteUser(ar.email)
                }
            }
        }
    }

    fun setFriendList(data: MutableList<FriendModel>) {
        friendList.value = data
    }

}
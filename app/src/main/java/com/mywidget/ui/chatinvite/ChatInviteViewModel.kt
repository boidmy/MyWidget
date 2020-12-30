package com.mywidget.ui.chatinvite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FriendModel
import com.mywidget.data.model.RoomDataModel
import javax.inject.Inject

class ChatInviteViewModel @Inject constructor(
    private val repository: ChatInviteRepository) : ViewModel() {

    var myId: String = ""
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()

    fun setChatRoomInformation(roomDataModel: RoomDataModel) {
        repository.setChatRoomInformation(roomDataModel)
    }

    fun selectFriendList() {
        friendList = repository.selectFriendList(myId)
    }

    fun myId(userEmail: String) {
        myId = userEmail
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

    fun setFriendList(data: ArrayList<FriendModel>) {
        friendList.value = data
    }

}
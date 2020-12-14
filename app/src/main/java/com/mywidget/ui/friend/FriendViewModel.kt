package com.mywidget.ui.friend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FriendModel
import javax.inject.Inject

class FriendViewModel @Inject constructor(private val repository: FriendRepository) : ViewModel() {

    var userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    var friendAddDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var myId: String = ""
    var friendList: MutableLiveData<ArrayList<FriendModel>> = MutableLiveData()

    fun setUserExistenceChk() {
        userExistenceChk = repository.setUserExistenceChk()
    }

    fun friendAddDialogVisibility(flag: Boolean) {
        friendAddDialogVisibility.value = flag
    }

    fun userExistenceChk(email: String, explanation: String) {
        repository.userExistenceChk(email, myId, explanation)
    }

    fun selectFriendList() {
        friendList = repository.selectFriendList(myId)
    }

    fun deleteFriend(email: String) {
        repository.deleteFriend(myId, email)
    }
}
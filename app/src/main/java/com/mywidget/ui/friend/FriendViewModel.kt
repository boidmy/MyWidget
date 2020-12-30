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

    var deleteFriend: MutableLiveData<String> = MutableLiveData()
    var deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    var friendUpdateDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var friendUpdateModel: MutableLiveData<FriendModel> = MutableLiveData()

    fun myId(email: String) {
        myId = repository.myId(email)
    }

    fun setFriendUpdateDialogVisibility() {
        friendUpdateDialogVisibility = repository.setFriendUpdateDialogVisibility()
    }

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
        friendList = repository.selectFriendList()
    }

    fun setFavorites(email: String, onOffChk: Boolean) {
        repository.setFavorites(email, onOffChk)
    }

    fun setDeleteFriendEmail(email: String) {
        deleteFriend.value = email
    }

    fun deleteFriend(email: String) {
        repository.deleteFriend(email)
        deleteDialogVisibility(false)
    }

    fun deleteDialogVisibility(flag: Boolean) {
        deleteDialogVisibility.value = flag
    }

    fun friendUpdateSelect(email: String) {
        friendUpdateModel = repository.friendUpdateSelect(email)
    }

    fun friendUpdate(email: String, nickName: String) {
        repository.friendUpdate(email, nickName)
    }
}
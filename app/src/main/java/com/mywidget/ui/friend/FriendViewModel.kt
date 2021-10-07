package com.mywidget.ui.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FriendModel
import javax.inject.Inject

class FriendViewModel @Inject constructor(private val repository: FriendRepository) : ViewModel() {

    private var _userExistenceChk: MutableLiveData<Boolean> = MutableLiveData()
    private val _friendAddDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var _myId: String = ""
    private var _friendList: MutableLiveData<List<FriendModel>> = MutableLiveData()
    private val _deleteFriend: MutableLiveData<String> = MutableLiveData()
    private val _deleteDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var _friendUpdateDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var _friendUpdateModel: MutableLiveData<FriendModel> = MutableLiveData()

    val userExistenceChk: LiveData<Boolean>
        get() = _userExistenceChk

    val friendAddDialogVisibility: LiveData<Boolean>
        get() = _friendAddDialogVisibility

    val myId: String
        get() = _myId

    val friendList: LiveData<List<FriendModel>>
        get() = _friendList

    val deleteFriend: LiveData<String>
        get() = _deleteFriend

    val deleteDialogVisibility: LiveData<Boolean>
        get() = _deleteDialogVisibility

    val friendUpdateDialogVisibility: LiveData<Boolean>
        get() = _friendUpdateDialogVisibility

    val friendUpdateModel: LiveData<FriendModel>
        get() = _friendUpdateModel

    fun myId(email: String) {
        _myId = repository.myId(email)
    }

    fun setFriendUpdateDialogVisibility() {
        _friendUpdateDialogVisibility = repository.setFriendUpdateDialogVisibility()
    }

    fun setUserExistenceChk() {
        _userExistenceChk = repository.setUserExistenceChk()
    }

    fun friendAddDialogVisibility(flag: Boolean) {
        _friendAddDialogVisibility.value = flag
    }

    fun userExistenceChk(email: String, explanation: String) {
        repository.userExistenceChk(email, myId, explanation)
    }

    fun selectFriendList() {
        _friendList = repository.selectFriendList()
    }

    fun setFavorites(email: String, onOffChk: Boolean) {
        repository.setFavorites(email, onOffChk)
    }

    fun setDeleteFriendEmail(email: String) {
        _deleteFriend.value = email
    }

    fun deleteFriend(email: String) {
        repository.deleteFriend(email)
        deleteDialogVisibility(false)
    }

    fun deleteDialogVisibility(flag: Boolean) {
        _deleteDialogVisibility.value = flag
    }

    fun friendUpdateSelect(email: String) {
        _friendUpdateModel = repository.friendUpdateSelect(email)
    }

    fun friendUpdate(email: String, nickName: String) {
        repository.friendUpdate(email, nickName)
    }
}
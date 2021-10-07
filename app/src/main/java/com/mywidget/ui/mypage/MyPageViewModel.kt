package com.mywidget.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MyPageViewModel @Inject constructor(val repository: MyPageRepository) : ViewModel() {

    private val _myId: MutableLiveData<String> = MutableLiveData()
    private var _nickName: MutableLiveData<String> = MutableLiveData()
    private var _updateConfirm: MutableLiveData<Boolean> = MutableLiveData()

    val myId: LiveData<String>
        get() = _myId

    val nickName: LiveData<String>
        get() = _nickName

    val updateConfirm: LiveData<Boolean>
        get() = _updateConfirm

    fun setMyId(email: String) {
        _myId.value = repository.setMyId(email)
    }

    fun selectMyNickName() {
        _nickName = repository.selectMyNickName()
    }

    fun updateNickName(name: String) {
        repository.updateNickName(name)
    }

    fun resetConfirm() {
        _updateConfirm = repository.resetConfirm()
    }
}
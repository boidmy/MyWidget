package com.mywidget.ui.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MyPageViewModel @Inject constructor(val repository: MyPageRepository) : ViewModel() {

    var myId: MutableLiveData<String> = MutableLiveData()
    var nickName: MutableLiveData<String> = MutableLiveData()
    var updateConfirm: MutableLiveData<Boolean> = MutableLiveData()

    fun setMyId(email: String) {
        myId.value = repository.setMyId(email)
    }

    fun selectMyNickName() {
        nickName = repository.selectMyNickName()
    }

    fun updateNickName(name: String) {
        repository.updateNickName(name)
    }

    fun resetConfirm() {
        updateConfirm = repository.resetConfirm()
    }
}
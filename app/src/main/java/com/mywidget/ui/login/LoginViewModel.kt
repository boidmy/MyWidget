package com.mywidget.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String, uid: String, nickname: String) {
        repository.singUpFirebase(email, uid, nickname)
    }

    fun loginSetToken(email: String) {
        repository.signUpToken(email)
    }

    fun setSignUpComplete() {
        signUpComplete = repository.setSignUpComplete()
    }
}
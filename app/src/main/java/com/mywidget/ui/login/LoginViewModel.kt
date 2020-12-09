package com.mywidget.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String, uid: String) {
        signUpComplete = repository.singUpFirebase(email, uid)
    }

    fun loginSetToken(email: String) {
        repository.signUpToken(email)
    }
}
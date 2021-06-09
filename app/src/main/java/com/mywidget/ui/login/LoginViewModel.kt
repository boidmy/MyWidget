package com.mywidget.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _signUpComplete: LiveData<Boolean> = repository.signUpComplete
    private val _data: MutableLiveData<String> = MutableLiveData()
    private val _forgotPasswordDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val signUpComplete: LiveData<Boolean>
        get() = _signUpComplete

    val data: MutableLiveData<String>
        get() = _data

    val forgotPasswordDialogVisibility: MutableLiveData<Boolean>
        get() = _forgotPasswordDialogVisibility

    fun singUpFirebase(email: String, uid: String, nickname: String) {
        repository.singUpFirebase(email, uid, nickname)
    }

    fun loginSetToken(email: String) {
        repository.signUpToken(email)
    }

    fun forgotPasswordDialogVisibility(flag: Boolean) {
        forgotPasswordDialogVisibility.value = flag
    }
}
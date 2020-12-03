package com.mywidget.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository) : ViewModel() {

    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String) {
        signUpComplete = signUpRepository.singUpFirebase(email)
    }
}
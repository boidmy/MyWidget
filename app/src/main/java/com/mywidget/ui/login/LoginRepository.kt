package com.mywidget.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.mywidget.data.model.UserData
import util.Util.replacePointToComma
import javax.inject.Inject
import javax.inject.Named

class LoginRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    @Inject @Named("User") lateinit var userRef: DatabaseReference
    private val _signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    val signUpComplete: LiveData<Boolean>
        get() = _signUpComplete

    fun singUpFirebase(email: String, uid: String, nickname: String) {
        val mEmail = replacePointToComma(email)
        val value = UserData(email, "", uid, nickname)
        userRef.child(mEmail).setValue(value)
        signUpToken(email)
        _signUpComplete.value = true
    }

    fun signUpToken(email: String) {
        val mEmail = replacePointToComma(email)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token: String = task.result.toString()
            userRef.child(mEmail).child("token").setValue(token)
        }
    }

    fun setSignUpComplete(): MutableLiveData<Boolean> {
        return _signUpComplete
    }
}
package com.mywidget.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.mywidget.data.model.UserData
import javax.inject.Inject

class LoginRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String, uid: String): MutableLiveData<Boolean> {
        val mEmail = email.replace(".", ",")
        val value = UserData(email, "", uid, "", "")
        userRef.child(mEmail).setValue(value)
        return signUpComplete
    }
}
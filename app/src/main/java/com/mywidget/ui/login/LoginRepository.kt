package com.mywidget.ui.login

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.mywidget.data.model.UserData
import util.Util.replacePointToComma
import javax.inject.Inject

class LoginRepository @Inject constructor() {

    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }
    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String, uid: String): MutableLiveData<Boolean> {
        val mEmail = replacePointToComma(email)
        val value = UserData(email, "", uid, "")
        userRef.child(mEmail).setValue(value)
        signUpToken(email)
        return signUpComplete
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
}
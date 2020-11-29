package com.mywidget.ui.signup

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class SignUpRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy {database.child("User")}
    var signUpComplete: MutableLiveData<Boolean> = MutableLiveData()
    var data: MutableLiveData<String> = MutableLiveData()

    fun singUpFirebase(email: String, password: String): MutableLiveData<Boolean> {
        userRef.child(email).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount > 0) {
                    //이미 존재하는 계정
                } else {
                    val mEmail: List<String>? = email.split("@")
                    var userId = ""
                    if (mEmail?.size ?: 0 > 0) {
                        userId = mEmail?.get(0) ?: ""
                    }

                    val result: HashMap<String, String> = hashMapOf()
                    result["id"] = ""
                    result["email"] = email
                    result["token"] = ""
                    result["friendName"] = ""
                    result["nickName"] = ""
                    result["password"] = password
                    userRef.child(userId).setValue(result)
                    signUpComplete.value = true
                }
            }

        })
        return signUpComplete
    }
}
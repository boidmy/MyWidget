package com.mywidget.ui.friend

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import util.Util
import javax.inject.Inject

class FriendRepository @Inject constructor() {
    @Inject lateinit var database: DatabaseReference
    private val userRef: DatabaseReference by lazy { database.child("User") }

    fun userExistenceChk(email: String) {
        userRef.child(Util.replacePointToComma(email))
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val userEmail: String = snapshot.child("email").value.toString()
                        val userToken: String = snapshot.child("token").value.toString()

                    } else {

                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
package com.mywidget.extension

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.model.FriendModel
import util.CalendarUtil

fun friendListExtension(friendRef: DatabaseReference
                        , mutableData: MutableLiveData<List<FriendModel>>) {
    friendRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val favorites = snapshot.child("favorites").value
            val array = arrayListOf<FriendModel>()
            for (snap: DataSnapshot in snapshot.child("friendList").children) {
                val friendModel = snap.getValue(FriendModel::class.java)
                friendModel?.let {
                    friendModel.favorites = friendModel.email == favorites
                    array.add(friendModel)
                }
            }
            mutableData.value = array
        }
        override fun onCancelled(error: DatabaseError) {}
    })
}

fun favoritesMessageExtension(favoritesRef: DatabaseReference
                              , mutableData: MutableLiveData<FavoritesData>) {
    favoritesRef.limitToLast(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) mutableData.value = null
                for (snap: DataSnapshot in snapshot.children) {
                    val value = snap.getValue(FavoritesData::class.java)
                    value?.let {
                        value.date = CalendarUtil.yearDateFormat(value.date)
                        mutableData.value = value
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
}
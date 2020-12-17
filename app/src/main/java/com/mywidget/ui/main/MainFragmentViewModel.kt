package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.room.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    var memoData: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<String> = MutableLiveData()
    var favoritesMessageMe: MutableLiveData<FavoritesData> = MutableLiveData()
    var favoritesMessageFriend: MutableLiveData<FavoritesData> = MutableLiveData()
    var myId: MutableLiveData<String> = MutableLiveData()

    var memoDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var loveDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val guidTextVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistenceMyFriend: MutableLiveData<String> = MutableLiveData()

    fun insertMemo(memo: String, data: String) {
        Thread {
            repository.insertMemo(memo, data)
            selectMemo()
        }.start()
    }

    fun deletMemo(memo: String) {
        Thread {
            repository.deleteMemo(memo)
            selectMemo()
        }.start()
    }

    fun selectMemo() {
        memoData.postValue(repository.selectMemo())
    }

    fun addLoveDay(data: String) {
        Thread {
            repository.addLoveDay(data)
            selectLoveDay()
        }.start()
    }

    fun selectLoveDay() {
        loveday.postValue(repository.selectLoveDay())
    }

    fun favoritesResetMe() {
        favoritesMessageMe = repository.favoritesResetMe()
    }

    fun favoritesResetFriend() {
        favoritesMessageFriend = repository.favoritesResetFriend()
    }

    fun favoritesMessageMe(friendEmail: String){
        if(friendEmail.isEmpty()) {
            repository.favoritesNoneMessageMe()
            repository.favoritesNoneMessageFriend()
        } else {
            repository.favoritesMessageMe(friendEmail)
            repository.favoritesMessageFriend(friendEmail)
        }
    }

    fun favoritesExistenceMyFriend() {
        favoritesExistenceMyFriend = repository.favoritesExistenceMyFriend()
    }

    fun logout(email: String) {
        repository.logout(email)
    }

    fun myIdReset() {
        myId = repository.myIdReset()
    }

    fun myId(email: String) {
        repository.myId(email)
    }

    fun guidTextVisibility(flag: Boolean) {
        guidTextVisibility.value = flag
    }

    fun memoDialogVisibility(flag: Boolean) {
        memoDialogVisibility.value = flag
    }

    fun loveDayDialogVisibility(flag: Boolean) {
        loveDayDialogVisibility.value = flag
    }

    fun favoritesDialogVisibility(flag: Boolean) {
        favoritesDialogVisibility.value = flag
    }

    fun favoritesMessage(text: String) {
        repository.favoritesMessage(text)
    }

    fun favoritesExistence() {
        favoritesExistence = repository.favoritesExistence()
    }
}
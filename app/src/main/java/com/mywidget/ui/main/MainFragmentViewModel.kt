package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    var memoData: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<String> = MutableLiveData()
    var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var message: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var leftString: MutableLiveData<LmemoData> = MutableLiveData()
    var rightString: MutableLiveData<LmemoData> = MutableLiveData()
    var myId: MutableLiveData<String> = MutableLiveData()

    var memoDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var loveDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val guidTextVisibility: MutableLiveData<Boolean> = MutableLiveData()
    var favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()

    fun insertMemo(memo: String, data: String) {
        Thread(Runnable {
            repository.insertMemo(memo, data)
            selectMemo()
        }).start()
    }

    fun deletMemo(memo: String) {
        Thread(Runnable {
            repository.deleteMemo(memo)
            selectMemo()
        }).start()
    }

    fun selectMemo() {
        memoData.postValue(repository.selectMemo())
    }

    fun addLoveDay(data: String) {
        Thread(Runnable {
            repository.addLoveDay(data)
            selectLoveDay()
        }).start()
    }

    fun selectLoveDay() {
        loveday.postValue(repository.selectLoveDay())
    }

    fun favoritesMessageMe(name: String){
        leftMessage = repository.favoritesMessageMe(name)
    }

    fun favoritesMessageFriend(name: String) {
        rightMessage = repository.favoritesMessageFriend(name)
    }

    fun leftClick() {
        message.value = leftMessage.value
    }

    fun rightClick() {
        message.value = rightMessage.value
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

    override fun onCleared() {
        repository.rxClear()
        super.onCleared()
    }
}
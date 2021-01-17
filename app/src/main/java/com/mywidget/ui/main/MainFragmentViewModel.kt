package com.mywidget.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.model.UserData
import com.mywidget.data.room.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _memoData: MutableLiveData<List<Memo>> = MutableLiveData()
    private val _loveDay: MutableLiveData<String> = MutableLiveData()
    private var _favoritesMessageMe: MutableLiveData<FavoritesData> = MutableLiveData()
    private var _favoritesMessageFriend: MutableLiveData<FavoritesData> = MutableLiveData()
    private var _myId: MutableLiveData<String> = MutableLiveData()
    private val _memoDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _loveDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _favoritesDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private var _favoritesExistence: MutableLiveData<Boolean> = MutableLiveData()
    private var _favoritesExistenceMyFriend: MutableLiveData<UserData> = MutableLiveData()
    private val _deleteDDayDialog: MutableLiveData<Memo> = MutableLiveData()
    private val _deleteDDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _dDayDetail: MutableLiveData<Int> = MutableLiveData()

    val memoData: MutableLiveData<List<Memo>>
        get() = _memoData

    val loveDay: MutableLiveData<String>
        get() = _loveDay

    val favoritesMessageMe: MutableLiveData<FavoritesData>
        get() = _favoritesMessageMe

    val favoritesMessageFriend: MutableLiveData<FavoritesData>
        get() = _favoritesMessageFriend

    val myId: MutableLiveData<String>
        get() = _myId

    val memoDialogVisibility: MutableLiveData<Boolean>
        get() = _memoDialogVisibility

    val loveDayDialogVisibility: MutableLiveData<Boolean>
        get() = _loveDayDialogVisibility

    val favoritesDialogVisibility: MutableLiveData<Boolean>
        get() = _favoritesDialogVisibility

    val favoritesExistence: MutableLiveData<Boolean>
        get() = _favoritesExistence

    val favoritesExistenceMyFriend: MutableLiveData<UserData>
        get() = _favoritesExistenceMyFriend

    val deleteDDayDialog: MutableLiveData<Memo>
        get() = _deleteDDayDialog

    val deleteDDayDialogVisibility: MutableLiveData<Boolean>
        get() = _deleteDDayDialogVisibility

    val dDayDetail: MutableLiveData<Int>
        get() = _dDayDetail

    fun insertMemo(memo: Memo) {
        Thread {
            repository.insertMemo(memo)
            selectMemo()
        }.start()
    }

    fun deleteMemo(data: Memo) {
        Thread {
            repository.deleteMemo(data)
            selectMemo()
        }.start()
        deleteDDayDialogVisibility(false)
    }

    fun updateMemo(data: Memo, updateMemo: String) {
        Thread {
            repository.updateMemo(data, updateMemo)
            selectMemo()
        }.start()
    }

    fun deleteDDayDialogVisibility(flag: Boolean) {
        deleteDDayDialogVisibility.value = flag
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
        loveDay.postValue(repository.selectLoveDay())
    }

    fun favoritesResetMe() {
        _favoritesMessageMe = repository.favoritesResetMe()
    }

    fun favoritesResetFriend() {
        _favoritesMessageFriend = repository.favoritesResetFriend()
    }

    fun favoritesSelectMessage(friendEmail: String){
        if(friendEmail.isEmpty()) {
            repository.favoritesNoneMessageMe()
            repository.favoritesNoneMessageFriend()
        } else {
            repository.favoritesMessageMe(friendEmail)
            repository.favoritesMessageFriend(friendEmail)
        }
    }

    fun favoritesExistenceMyFriend() {
        _favoritesExistenceMyFriend = repository.favoritesExistenceMyFriend()
    }

    fun logout(email: String) {
        repository.logout(email)
        myId("")
    }

    fun myIdReset() {
        _myId = repository.myIdReset()
    }

    fun myId(email: String) {
        repository.myId(email)
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

    fun favoritesInsertMessage(text: String) {
        repository.favoritesInsertMessage(text)
    }

    fun favoritesExistence() {
        _favoritesExistence = repository.favoritesExistence()
    }

    fun dDayDetail(index: Int) {
        dDayDetail.value = index
    }

    fun deleteDDayDialog(data: Memo) {
        deleteDDayDialog.value = data
    }
}
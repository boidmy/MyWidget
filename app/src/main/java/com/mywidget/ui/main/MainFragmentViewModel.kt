package com.mywidget.ui.main

import androidx.lifecycle.*
import com.mywidget.data.model.FavoritesData
import com.mywidget.data.model.UserData
import com.mywidget.data.room.*
import kotlinx.coroutines.*
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _memoData: MutableLiveData<List<Memo>> = MutableLiveData()
    private val _loveDay: MutableLiveData<String> = MutableLiveData()
    private val _memoDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _loveDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _favoritesDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _myId: LiveData<String> = repository.myId
    private val _favoritesExistence: LiveData<Boolean> = repository.favoritesExistence
    private val _favoritesExistenceMyFriend: LiveData<UserData> = Transformations.switchMap(_myId) {
        repository.favoritesExistenceMyFriend()
    }
    private val _favoritesMessageFriend: LiveData<FavoritesData> =
        Transformations.switchMap(_favoritesExistenceMyFriend) {
            repository.favoritesMessageFriend(it.email)
        }
    private val _favoritesMessageMe: LiveData<FavoritesData> =
        Transformations.switchMap(_favoritesExistenceMyFriend) {
            repository.favoritesMessageMe(it.email)
        }
    private val _deleteDDayDialog: MutableLiveData<Memo> = MutableLiveData()
    private val _deleteDDayDialogVisibility: MutableLiveData<Boolean> = MutableLiveData()
    private val _dDayDetail: MutableLiveData<Int> = MutableLiveData()

    val memoData: LiveData<List<Memo>>
        get() = _memoData

    val loveDay: LiveData<String>
        get() = _loveDay

    val favoritesMessageMe: LiveData<FavoritesData>
        get() = _favoritesMessageMe

    val favoritesMessageFriend: LiveData<FavoritesData>
        get() = _favoritesMessageFriend

    val myId: LiveData<String>
        get() = _myId

    val memoDialogVisibility: LiveData<Boolean>
        get() = _memoDialogVisibility

    val loveDayDialogVisibility: LiveData<Boolean>
        get() = _loveDayDialogVisibility

    val favoritesDialogVisibility: LiveData<Boolean>
        get() = _favoritesDialogVisibility

    val favoritesExistence: LiveData<Boolean>
        get() = _favoritesExistence

    val favoritesExistenceMyFriend: LiveData<UserData>
        get() = _favoritesExistenceMyFriend

    val deleteDDayDialog: LiveData<Memo>
        get() = _deleteDDayDialog

    val deleteDDayDialogVisibility: LiveData<Boolean>
        get() = _deleteDDayDialogVisibility

    val dDayDetail: LiveData<Int>
        get() = _dDayDetail

    fun insertMemo(memo: Memo) {
        viewModelScope.launch {
            repository.insertMemo(memo)
            selectMemo()
        }
    }

    fun deleteMemo(data: Memo) {
        viewModelScope.launch {
            repository.deleteMemo(data)
            selectMemo()
        }
        deleteDDayDialogVisibility(false)
    }

    fun updateMemo(data: Memo, updateMemo: String) {
        viewModelScope.launch {
            repository.updateMemo(data, updateMemo)
            selectMemo()
        }
    }

    fun deleteDDayDialogVisibility(flag: Boolean) {
        _deleteDDayDialogVisibility.value = flag
    }

    suspend fun selectMemo() {
        _memoData.postValue(repository.selectMemo())
    }

    fun addLoveDay(data: String) {
        viewModelScope.launch {
            repository.addLoveDay(data)
            selectLoveDay()
        }
    }

    suspend fun selectLoveDay() {
        _loveDay.postValue(repository.selectLoveDay())
    }

    fun logout(email: String) {
        repository.logout(email)
        myId("")
    }

    fun myId(email: String) {
        repository.myId(email)
    }

    fun memoDialogVisibility(flag: Boolean) {
        _memoDialogVisibility.value = flag
    }

    fun loveDayDialogVisibility(flag: Boolean) {
        _loveDayDialogVisibility.value = flag
    }

    fun favoritesDialogVisibility(flag: Boolean) {
        _favoritesDialogVisibility.value = flag
    }

    fun favoritesInsertMessage(text: String) {
        repository.favoritesInsertMessage(text)
    }

    fun dDayDetail(index: Int) {
        _dDayDetail.value = index
    }

    fun deleteDDayDialog(data: Memo) {
        _deleteDDayDialog.value = data
    }
}
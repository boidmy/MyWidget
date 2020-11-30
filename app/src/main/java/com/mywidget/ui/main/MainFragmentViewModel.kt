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

    var dialogVisible: MutableLiveData<Boolean> = MutableLiveData()

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

    fun messageLeft(name: String){
        leftMessage = repository.messageLeft(name)
    }

    fun messageRight(name: String) {
        rightMessage = repository.messageRight(name)
    }

    fun leftClick() {
        message.value = leftMessage.value
    }

    fun rightClick() {
        message.value = rightMessage.value
    }

    override fun onCleared() {
        repository.rxClear()
        super.onCleared()
    }

    /*
    * private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Lmemo")

    fun message() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                var arrayRight = arrayListOf<Ldata>()
                var arrayLeft = arrayListOf<Ldata>()

                for (ds in data.child("콩이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    product?.let {
                        arrayRight.add(it)
                    }
                }

                for (ds in data.child("뿡이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    product?.let {
                        arrayLeft.add(it)
                    }
                }

                leftMessage.value = arrayLeft
                rightMessage.value = arrayRight
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }*/
}
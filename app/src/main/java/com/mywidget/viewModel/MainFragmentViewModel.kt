package com.mywidget.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.*
import com.mywidget.repository.MessageRepository

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var memoData: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<String> = MutableLiveData()
    var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var message: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var leftString : MutableLiveData<LmemoData> = MutableLiveData()
    var rightString : MutableLiveData<LmemoData> = MutableLiveData()
    var repository = MessageRepository(application)

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

    fun messageLeft(name: String) : MutableLiveData<List<LmemoData>> {
        leftMessage = repository.messageLeft(name)
        return leftMessage
    }

    fun messageRight(name: String) : MutableLiveData<List<LmemoData>> {
        rightMessage = repository.messageRight(name)
        return rightMessage
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
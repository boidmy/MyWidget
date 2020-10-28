package com.mywidget.viewModel

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.data.model.LmemoData
import com.mywidget.data.room.*
import com.mywidget.repository.MessageRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var data: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<String> = MutableLiveData()
    var leftMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var rightMessage: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var message: MutableLiveData<List<LmemoData>> = MutableLiveData()
    var leftString : MutableLiveData<LmemoData> = MutableLiveData()
    var rightString : MutableLiveData<LmemoData> = MutableLiveData()

    var memoDB: MemoDB? = null
    var loveDayDB: LoveDayDB? = null

    var repository: MessageRepository = MessageRepository.instance()

    fun insertMemo(memo: String, data: String) {
        memoDB?.memoDao()?.insert(Memo(null, memo, data))
        selectMemo()
    }

    fun deletMemo(memo: String) {
        memoDB?.memoDao()?.delete(memo)
        selectMemo()
    }

    fun selectMemo() {
        data.postValue(memoDB?.memoDao()?.getUser())
    }

    fun insertLoveDay(data: String) {
        loveDayDB?.loveDayDao()?.insert(LoveDay(null, data))
        selectLoveDay()
    }

    fun selectLoveDay() {
        loveday.postValue(repository.lovedayFormatt(loveDayDB?.loveDayDao()?.getData()))
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

    fun rxClear() {
        memoDB?.destroyInstance()
        loveDayDB?.destroyInstance()
        repository.rxClear()
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
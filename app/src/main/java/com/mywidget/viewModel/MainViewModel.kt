package com.mywidget.viewModel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var data: MutableLiveData<List<Memo>> = MutableLiveData()
    var loveday: MutableLiveData<LoveDay> = MutableLiveData()

    var memoDB: MemoDB? = null
    var loveDayDB: LoveDayDB? = null

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
    }

    fun selectLoveDay() {
        loveday.postValue(loveDayDB?.loveDayDao()?.getData())
    }
}
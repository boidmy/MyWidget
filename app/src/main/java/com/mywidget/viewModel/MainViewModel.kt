package com.mywidget.viewModel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mywidget.data.room.Memo
import com.mywidget.data.room.MemoDB
import com.mywidget.data.room.UserDB

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var data: MutableLiveData<List<Memo>> = MutableLiveData()

    var memoDB: MemoDB? = null

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
}
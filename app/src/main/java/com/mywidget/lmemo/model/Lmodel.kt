package com.mywidget.lmemo.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.mywidget.lmemo.LmemoContract
import com.mywidget.lmemo.presenter.Lpresenter

class Lmodel(private var presenter: Lpresenter?) : LmemoContract.model {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun insertMemo(name:String, memo:String) {

    }

    override fun onDestroy(isChangingConfiguration: Boolean) {
        if(!isChangingConfiguration) {
            presenter = null
        }
    }



}
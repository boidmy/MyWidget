package com.mywidget.lmemo.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mywidget.lmemo.LmemoContract
import java.lang.ref.WeakReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Lpresenter(view: LmemoContract.View) : LmemoContract.presenter {

    var mModel: LmemoContract.model? = null
    private var mView = WeakReference(view)
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Lmemo")

    fun setModel(model:LmemoContract.model) {
        mModel = model
    }

    fun getView(): LmemoContract.View? {
        if(mView != null) {
            return mView.get()
        } else {
            throw NullPointerException("View is unavailable")
        }
    }

    override fun setView(view: LmemoContract.View) {
        mView = WeakReference(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun clickMemoadd(name:String, memo:String) {
        memo(name, memo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun memo(name:String, memo:String) {

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatted = current.format(formatter)

        var result: HashMap<String, String> = hashMapOf()

        result.put("memo", memo)
        result.put("date", formatted)

        database.child(name).push().setValue(result)
    }

}
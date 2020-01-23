package com.mywidget.lmemo

import android.content.Context

interface LmemoContract {

    interface View {
        fun notifyDataSetChanged()
        fun context() :Context
        fun nickNameAdd(nickName: String)
    }

    interface presenter {
        fun setView(view:View)
        fun clickMemoadd(name:String, memo:String)
        fun selectNickName()
    }

    interface model {
        fun onDestroy(isChangingConfiguration: Boolean)
        fun insertMemo(name:String, memo:String)
    }
}
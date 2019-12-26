package com.mywidget.lmemo

interface LmemoContract {

    interface View {
        fun notifyDataSetChanged()
    }

    interface presenter {
        fun setView(view:View)
        fun clickMemoadd(name:String, memo:String)
    }

    interface model {
        fun onDestroy(isChangingConfiguration: Boolean)
        fun insertMemo(name:String, memo:String)
    }
}
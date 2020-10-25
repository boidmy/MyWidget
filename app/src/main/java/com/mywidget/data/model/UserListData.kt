package com.mywidget.data.model

class UserListData {

    internal var userList: List<USERLISTITEM> = arrayListOf()

    inner class USERLISTITEM {
        var userName: String? = ""
        var phonNumber: String? = ""
    }
}

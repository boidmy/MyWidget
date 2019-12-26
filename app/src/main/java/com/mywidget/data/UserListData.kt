package com.mywidget.data

class UserListData {

    internal var userList: List<USERLISTITEM> = arrayListOf()

    inner class USERLISTITEM {
        var userName: String? = ""
        var phonNumber: String? = ""
    }
}

package com.mywidget.data.model

import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.Interface.DiffUtilSelectorDataInterface
import java.io.Serializable

data class FriendModel(
    var email: String = "",
    var nickName: String = "",
    var selector: Boolean = false,
    var favorites: Boolean = false
): Serializable, DiffUtilDataInterface, DiffUtilSelectorDataInterface {
    override fun keyValue(): String {
        return email
    }

    override fun contentSelectorValue(): String {
        return selector.toString()
    }

    override fun contentValue(): String {
        return favorites.toString()
    }

}
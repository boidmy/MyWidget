package com.mywidget.common

import androidx.recyclerview.widget.DiffUtil
import com.mywidget.data.Interface.ChatDataInterface

object DiffUtilCallBack: DiffUtil.ItemCallback<ChatDataInterface>() {
    override fun areItemsTheSame(oldItem: ChatDataInterface, newItem: ChatDataInterface): Boolean {
        return oldItem.keyValue() == newItem.keyValue()
    }

    override fun areContentsTheSame(
        oldItem: ChatDataInterface,
        newItem: ChatDataInterface
    ): Boolean {
        return oldItem.contentValue() == newItem.contentValue()
    }

}

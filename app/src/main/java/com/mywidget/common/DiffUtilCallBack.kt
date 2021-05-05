package com.mywidget.common

import androidx.recyclerview.widget.DiffUtil
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.Interface.DiffUtilSelectorDataInterface

object DiffUtilCallBack: DiffUtil.ItemCallback<DiffUtilDataInterface>() {
    override fun areItemsTheSame(oldItem: DiffUtilDataInterface, newItem: DiffUtilDataInterface): Boolean {
        return oldItem.keyValue() == newItem.keyValue()
    }

    override fun areContentsTheSame(
        oldItem: DiffUtilDataInterface,
        newItem: DiffUtilDataInterface
    ): Boolean {
        return oldItem.contentValue() == newItem.contentValue()
    }
}

object DiffUtilSelectorCallBack: DiffUtil.ItemCallback<DiffUtilSelectorDataInterface>() {
    override fun areItemsTheSame(oldItem: DiffUtilSelectorDataInterface, newItem: DiffUtilSelectorDataInterface): Boolean {
        return oldItem.keyValue() == newItem.keyValue()
    }

    override fun areContentsTheSame(
        oldItem: DiffUtilSelectorDataInterface,
        newItem: DiffUtilSelectorDataInterface
    ): Boolean {
        return oldItem.contentSelectorValue() == newItem.contentSelectorValue()
    }
}

package com.mywidget.common

import androidx.recyclerview.widget.DiffUtil
import com.mywidget.data.Interface.DiffUtilDataInterface

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

package com.mywidget.ui.chat.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.ChatDataInterface
import com.mywidget.data.model.ChatData
import com.mywidget.data.model.ChatDataModel
import com.mywidget.ui.chat.ChatViewModel
import com.mywidget.databinding.ChatLeftBinding
import com.mywidget.databinding.ChatRightBinding

class ChatAdapter(val viewModel: ChatViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LEFTCHAT = 0
    private val RIGHTCHAT = 1
    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        return if (viewType == RIGHTCHAT) {
            val bind = ChatRightBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ChatRightViewHolder(bind, viewModel)
        } else {
            val bind = ChatLeftBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ChatLeftViewHolder(bind, viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val id = (diffUtil.currentList[position] as ChatData).chatDataModel.id
        return if (id == viewModel.myId) {
            RIGHTCHAT
        } else {
            LEFTCHAT
        }
    }

    override fun getItemCount(): Int {
        return diffUtil.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = diffUtil.currentList[position]
        (data as ChatData).let {
            if(holder is ChatRightViewHolder) {
                holder.bindView(it.chatDataModel)
            } else if(holder is ChatLeftViewHolder){
                holder.bindView(it.chatDataModel)
            }
        }
    }

    fun setData(data: List<ChatData>) {
        val item: ArrayList<ChatData> = arrayListOf()
        item.addAll(data)
        //diffUtil.submitList(null)
        diffUtil.submitList(item as List<ChatDataInterface>?)
    }
}
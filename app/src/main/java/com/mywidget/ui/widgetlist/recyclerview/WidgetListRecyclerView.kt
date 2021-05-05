package com.mywidget.ui.widgetlist.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.common.DiffUtilCallBack
import com.mywidget.data.Interface.DiffUtilDataInterface
import com.mywidget.data.room.User
import com.mywidget.databinding.WidgetUserRvItemBinding
import com.mywidget.ui.base.ViewHolderBase
import com.mywidget.ui.widgetlist.WidgetListViewModel

class WidgetListRecyclerView(viewModel: WidgetListViewModel)
    : RecyclerView.Adapter<MyViewHolder>() {
    private var mViewModel = viewModel
    private val diffUtil = AsyncListDiffer(this, DiffUtilCallBack)

    private fun currentList(): MutableList<DiffUtilDataInterface> = diffUtil.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(parent)
    }

    override fun getItemCount(): Int = currentList().size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(currentList()[position] as User , mViewModel)
    }

    fun setData(data: List<User>) {
        val item: MutableList<User> = mutableListOf()
        item.addAll(data)
        diffUtil.submitList(item as List<DiffUtilDataInterface>)
    }
}

class MyViewHolder(parent: ViewGroup)
    : ViewHolderBase<WidgetUserRvItemBinding>(parent, R.layout.widget_user_rv_item) {

    fun bindView(data: User?, widgetViewModel: WidgetListViewModel) {
        binding.apply {
            item = data
            viewModel = widgetViewModel
            executePendingBindings()
        }
    }
}

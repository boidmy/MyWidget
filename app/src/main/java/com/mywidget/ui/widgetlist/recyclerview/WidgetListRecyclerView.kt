package com.mywidget.ui.widgetlist.recyclerview

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.data.room.User
import com.mywidget.databinding.WidgetUserRvItemBinding
import com.mywidget.ui.widgetlist.WidgetListViewModel

class WidgetListRecyclerView(viewModel: WidgetListViewModel)
    : RecyclerView.Adapter<MyViewHolder>() {
    private var mViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bind = WidgetUserRvItemBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)
        return MyViewHolder(bind)
    }

    override fun getItemCount(): Int = mViewModel.data.value?.size?: 0

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(mViewModel.data.value?.get(position), mViewModel)
    }
}

class MyViewHolder(val binding: WidgetUserRvItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(data: User?, widgetViewModel: WidgetListViewModel) {
        binding.apply {
            item = data
            viewModel = widgetViewModel
            executePendingBindings()
        }
    }
}

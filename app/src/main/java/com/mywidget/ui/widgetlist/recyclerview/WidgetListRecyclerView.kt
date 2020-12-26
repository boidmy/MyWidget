package com.mywidget.ui.widgetlist.recyclerview

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.databinding.WidgetUserRvItemBinding
import com.mywidget.ui.widgetlist.WidgetListViewModel

class WidgetListRecyclerView(viewModel: WidgetListViewModel)
    : RecyclerView.Adapter<MyViewHolder>() {
    private var mViewModel = viewModel

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(mViewModel, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bind = WidgetUserRvItemBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)
        return MyViewHolder(bind)
    }

    override fun getItemCount(): Int = mViewModel.data.value?.size?: 0
}

class MyViewHolder(val binding: WidgetUserRvItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindView(mViewModel: WidgetListViewModel?, mPosition: Int) {
        binding.apply {
            position = mPosition
            viewModel = mViewModel
            executePendingBindings()
            val alert = AlertDialog.Builder(root.context)
            deleteBtn.setOnClickListener {
                alert
                    .setTitle("삭제하실건가요?")
                    .setPositiveButton("삭제") { _, _ ->
                        viewModel?.deleteUser(userName.text.toString())
                    }
                    .setNegativeButton("취소") { _, _ ->
                    }.show()
            }
        }

    }
}

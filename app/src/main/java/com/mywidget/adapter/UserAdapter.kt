package com.mywidget.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.databinding.UserRvItemBinding
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.user_rv_item.view.*

class UserAdapter(viewModel: UserViewModel?)
    : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private var mViewModel = viewModel

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(mViewModel, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bind = UserRvItemBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)
        return MyViewHolder(bind)
    }

    override fun getItemCount(): Int = mViewModel?.data?.value?.size?: 0

    inner class MyViewHolder(val binding: UserRvItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindView(viewModel: UserViewModel?, position: Int) {
            binding.position = position
            binding.viewModel = viewModel
            binding.executePendingBindings()
            val alert = AlertDialog.Builder(itemView.context)
            itemView.delete_btn.setOnClickListener {
                alert
                    .setTitle("삭제할거에염?")
                    .setPositiveButton("삭제") { _, _ ->
                        binding.viewModel?.deleteUser(itemView.user_name.text.toString())
                    }
                    .setNegativeButton("취소") { _, _ ->
                    }.show()
            }
        }
    }
}

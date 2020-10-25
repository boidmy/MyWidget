package com.mywidget.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.model.UserListData
import com.mywidget.data.room.User
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.user_rv_item.view.*

class UserAdapter(context: Context, viewModel: UserViewModel?) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private val mContext = context
    private var mData: List<User>? = null
    private var mViewModel = viewModel

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_rv_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData?.size?: 0
    }

    fun setData(userListData: List<User>?) {
        mData = userListData
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int) {
            itemView.user_name.text = mData?.get(position)?.name
            itemView.user_phone.text = mData?.get(position)?.number

            var alert = AlertDialog.Builder(itemView.context)
            itemView.delete_btn.setOnClickListener {
                alert
                    .setTitle("삭제할거에염?")
                    .setPositiveButton("삭제") { _, _ ->
                        Thread(Runnable {
                            mViewModel?.deleteUser(itemView.user_name.text.toString())
                        }).start()
                    }
                    .setNegativeButton("취소") { _, _ ->
                    }
                    .show()
            }
        }
    }
}

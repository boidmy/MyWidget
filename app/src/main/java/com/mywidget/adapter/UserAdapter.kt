package com.mywidget.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.UserListData
import kotlinx.android.synthetic.main.user_rv_item.view.*

class UserAdapter(context: Context, callBack: UserACallBack) : RecyclerView.Adapter<UserAdapter.myViewHolder>() {
    val mContext = context
    private var mData: ArrayList<UserListData.USERLISTITEM>? = null
    private var mCallBack = callBack

    interface UserACallBack {
        fun callBackCall()
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_rv_item, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData?.size?: 0
    }

    fun setData(userListData: ArrayList<UserListData.USERLISTITEM>) {
        mData = userListData
        notifyDataSetChanged()
    }

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int) {
            itemView.user_name.text = mData?.get(position)?.userName
            itemView.user_phone.text = mData?.get(position)?.phonNumber

            var alert = AlertDialog.Builder(itemView.context)


            itemView.delete_btn.setOnClickListener {
                alert
                    .setTitle("삭제할거에염?")
                    .setPositiveButton("삭제") { _, _ ->
                        Util.deleteSql(itemView.context, itemView.user_name.text.toString())
                        mCallBack.callBackCall()
                    }
                    .setNegativeButton("취소") { _, _ ->
                    }
                    .show()
            }
        }
    }
}

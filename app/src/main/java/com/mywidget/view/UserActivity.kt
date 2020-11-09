package com.mywidget.view

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MyAppWidget
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.adapter.UserAdapter
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import com.mywidget.databinding.ActivityUserBinding
import com.mywidget.databinding.MainPhoneDialogBinding
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.*

class UserActivity : BaseActivity<UserViewModel, ActivityUserBinding>() {
    private var mAdapter: UserAdapter? = null

    override val layout: Int
        get() = R.layout.activity_user

    override fun getViewModel(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

    companion object {
        @BindingAdapter("items")
        @JvmStatic
        fun adapter(recyclerView: RecyclerView?, data: MutableLiveData<List<User>>) {
            val adapter: UserAdapter = recyclerView?.adapter as UserAdapter
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        mAdapter = UserAdapter(viewModel)
        binding.userRv.adapter = mAdapter
        selectUser()

        add_txt.setOnClickListener(onClickListener)
    }

    private fun selectUser() {
        Thread(Runnable {
            viewModel.selectUser()
        }).start()
    }

    private val onClickListener = View.OnClickListener {
        val dialog = Dialog(this)
        val userBinding = MainPhoneDialogBinding.inflate(LayoutInflater.from(this))
        userBinding.viewModel = viewModel
        dialog.setContentView(userBinding.root)
        viewModel.dialogVisible.value = true
        dialog.show()
        Util.upKeyboard(this)
        viewModel.dialogVisible.observe(this, Observer {
            if(!it) dialog.dismiss()
            Util.downKeyboard(this)
        })
    }
}

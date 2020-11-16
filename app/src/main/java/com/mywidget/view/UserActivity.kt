package com.mywidget.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.adapter.UserAdapter
import com.mywidget.data.room.User
import com.mywidget.databinding.ActivityUserBinding
import com.mywidget.databinding.MainPhoneDialogBinding
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*
import javax.inject.Inject

class UserActivity : BaseActivity<UserViewModel, ActivityUserBinding>() {
    private var mAdapter: UserAdapter? = null

    @Inject lateinit var factory: ViewModelProvider.NewInstanceFactory

    private val viewModel by viewModels<UserViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_user

    override fun getViewModel(): Class<UserViewModel> {
        return UserViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).getApplicationCompoenet()
            .userActivityComponentBuilder.create().inject(this)

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

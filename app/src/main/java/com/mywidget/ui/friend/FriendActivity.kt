package com.mywidget.ui.friend

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityFriendBinding
import com.mywidget.databinding.FriendAddDialogBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.friend.recyclerview.FriendRecyclerView
import javax.inject.Inject

class FriendActivity : BaseActivity<ActivityFriendBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<FriendViewModel> { factory }
    private val friendAddDialogBinding by lazy {
        FriendAddDialogBinding.inflate(LayoutInflater.from(this))
    }
    private val friendDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.activity_friend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindView()
        friendAddDialog()
    }

    fun bindView() {
        binding.viewModel = viewModel
        binding.friendRv.adapter = FriendRecyclerView(viewModel)
    }

    fun friendAddDialog() {
        friendDialog.setContentView(friendAddDialogBinding.root)
    }

    fun onclickFriendAdd(v: View) {

    }
}
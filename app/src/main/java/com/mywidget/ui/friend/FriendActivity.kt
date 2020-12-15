package com.mywidget.ui.friend

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityFriendBinding
import com.mywidget.databinding.FriendAddDialogBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.friend.recyclerview.FriendRecyclerView
import kotlinx.android.synthetic.main.friend_add_dialog.*
import util.Util.toast
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
        with(loginEmail()) {
            viewModel.myId(this)
        }
        viewModel.selectFriendList()
    }

    private fun friendAddDialog() {
        friendAddDialogBinding.viewModel = viewModel
        friendDialog.setContentView(friendAddDialogBinding.root)
        userExistenceChk()
        viewModel.friendAddDialogVisibility.observe(this, Observer {
            if(it) {
                friendDialog.show()
                friendDialog.friendAddExplanationEdit.text = null
                friendDialog.friendAddEmailEdit.text = null
            } else friendDialog.dismiss()
        })
    }

    private fun userExistenceChk() {
        viewModel.setUserExistenceChk()
        viewModel.userExistenceChk.observe(this, Observer {
            if (it) {
                viewModel.friendAddDialogVisibility(false)
            } else {
                this.toast("이메일을 다시 확인해 주세요")
            }
        })
    }
}
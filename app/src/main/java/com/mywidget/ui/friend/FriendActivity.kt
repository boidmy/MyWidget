package com.mywidget.ui.friend

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mywidget.R
import com.mywidget.databinding.ActivityFriendBinding
import com.mywidget.databinding.DeleteConfirmDialogFriendBinding
import com.mywidget.databinding.FriendAddDialogBinding
import com.mywidget.databinding.FriendUpdateDialogBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.friend.recyclerview.FriendAdapter
import kotlinx.android.synthetic.main.friend_add_dialog.*
import util.Util.toast
import javax.inject.Inject


class FriendActivity : BaseActivity<ActivityFriendBinding>() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<FriendViewModel> { factory }
    @Inject lateinit var friendDialog: Dialog
    @Inject lateinit var friendAddDialogBinding: FriendAddDialogBinding
    @Inject lateinit var deleteDialog: Dialog
    @Inject lateinit var deleteDialogBinding: DeleteConfirmDialogFriendBinding
    @Inject lateinit var friendUpdateDialog: Dialog
    @Inject lateinit var friendUpdateDialogBinding: FriendUpdateDialogBinding

    override val layout: Int
        get() = R.layout.activity_friend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        friendAddDialog()
        deleteDialog()
        friendUpdateDialog()
    }

    fun bindView() {
        binding.viewModel = viewModel
        binding.friendRv.adapter = FriendAdapter(viewModel)
        (binding.friendRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        with(loginEmail()) {
            viewModel.myId(this)
        }
        viewModel.selectFriendList()
    }

    private fun friendUpdateDialog() {
        friendUpdateDialogBinding.viewModel = viewModel
        viewModel.setFriendUpdateDialogVisibility()
        friendUpdateDialog.setContentView(friendUpdateDialogBinding.root)
        viewModel.friendUpdateDialogVisibility.observe(this, Observer {
            if (it) friendUpdateDialog.show()
            else friendUpdateDialog.dismiss()
        })
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

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
        viewModel.deleteDialogVisibility.observe(this, Observer {
            if (it) deleteDialog.show()
            else deleteDialog.dismiss()
        })
        viewModel.deleteFriend.observe(this, Observer {
            deleteDialogBinding.email = it
            viewModel.deleteDialogVisibility(true)
        })
    }
}
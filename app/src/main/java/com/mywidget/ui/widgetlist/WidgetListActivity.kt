package com.mywidget.ui.widgetlist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.R
import util.Util
import com.mywidget.ui.widgetlist.recyclerview.WidgetListRecyclerView
import com.mywidget.databinding.ActivityUserBinding
import com.mywidget.databinding.MainPhoneDialogBinding
import com.mywidget.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user.*
import javax.inject.Inject

class WidgetListActivity : BaseActivity<ActivityUserBinding>() {
    private var mAdapter: WidgetListRecyclerView? = null

    @Inject lateinit var factory: ViewModelProvider.Factory

    val viewModel: WidgetListViewModel by lazy {
        ViewModelProvider(this, factory).get(WidgetListViewModel::class.java)}

    override val layout: Int
        get() = R.layout.activity_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        mAdapter = WidgetListRecyclerView(viewModel)
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

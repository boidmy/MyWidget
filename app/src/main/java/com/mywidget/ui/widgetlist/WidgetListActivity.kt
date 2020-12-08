package com.mywidget.ui.widgetlist

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.MyAppWidget
import com.mywidget.R
import util.Util
import com.mywidget.ui.widgetlist.recyclerview.WidgetListRecyclerView
import com.mywidget.databinding.ActivityUserBinding
import com.mywidget.databinding.MainPhoneDialogBinding
import com.mywidget.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONArray
import javax.inject.Inject

class WidgetListActivity : BaseActivity<ActivityUserBinding>() {
    private var mAdapter: WidgetListRecyclerView? = null

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var dialog: Dialog
    private val userBinding
            by lazy { MainPhoneDialogBinding.inflate(LayoutInflater.from(this)) }

    val viewModel by viewModels<WidgetListViewModel> { factory }

    override val layout: Int
        get() = R.layout.activity_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        mAdapter = WidgetListRecyclerView(viewModel)
        binding.userRv.adapter = mAdapter
        selectUser()
        setWidgetAddDialog()
        bindWidgetUpdate()

        add_txt.setOnClickListener(onClickListener)
    }

    private fun bindWidgetUpdate() {
        viewModel.widgetJsonArrayData.observe(this, Observer {
            viewModel.widgetJsonArrayData.value?.let {
                setWidgetSharedPreference(it)
                val intent = Intent(this, MyAppWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                this.sendBroadcast(intent)
            }
        })
    }

    private fun selectUser() {
        viewModel.setWidgetData()
        viewModel.setWidgetDataJsonArray()
        Thread(Runnable {
            viewModel.selectUser()
        }).start()
    }

    private fun setWidgetAddDialog() {
        dialog.setContentView(userBinding.root)
        userBinding.viewModel = viewModel
        viewModel.dialogVisible.observe(this, Observer {
            if(it) dialog.show()
            else {
                dialog.dismiss()
                Util.downKeyboard(this)
            }
        })
    }

    private val onClickListener = View.OnClickListener {
        viewModel.dialogVisible.value = true
        Util.upKeyboard(this)
    }

    private fun setWidgetSharedPreference(jsonArray: JSONArray) {
        val mSharedPreference
                = getSharedPreferences("widget", Context.MODE_PRIVATE)
        val editor = mSharedPreference.edit()
        editor.remove("data")
        editor.putString("data", jsonArray.toString())
        editor.apply()
    }
}

package com.mywidget.ui.widgetlist

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mywidget.MyAppWidget
import com.mywidget.R
import com.mywidget.ui.widgetlist.recyclerview.WidgetListRecyclerView
import com.mywidget.databinding.ActivityWidgetBinding
import com.mywidget.databinding.DeleteConfirmDialogFriendBinding
import com.mywidget.databinding.DeleteConfirmDialogWidgetBinding
import com.mywidget.databinding.WidgetAddDialogBinding
import com.mywidget.ui.base.BaseActivity
import kotlinx.android.synthetic.main.widget_add_dialog.*
import org.json.JSONArray
import util.Util.toast
import javax.inject.Inject

class WidgetListActivity : BaseActivity<ActivityWidgetBinding>() {
    private var mAdapter: WidgetListRecyclerView? = null

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val widgetDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }
    private val widgetDialogBinding
            by lazy { WidgetAddDialogBinding.inflate(LayoutInflater.from(this)) }

    val viewModel by viewModels<WidgetListViewModel> { factory }
    private val deleteDialogBinding by lazy {
        DeleteConfirmDialogWidgetBinding.inflate(LayoutInflater.from(this)) }
    private val deleteDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.activity_widget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        mAdapter = WidgetListRecyclerView(viewModel)
        binding.userRv.adapter = mAdapter
        permissionChk()
        selectUser()
        setWidgetAddDialog()
        bindWidgetUpdate()
        deleteDialog()
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
        Thread {
            viewModel.selectUser()
        }.start()
    }

    private fun setWidgetAddDialog() {
        widgetDialogBinding.viewModel = viewModel
        widgetDialog.setContentView(widgetDialogBinding.root)
        viewModel.dialogVisible.observe(this, Observer {
            if(it) {
                widgetDialog.widgetAddNameEdit.text = null
                widgetDialog.widgetAddPhoneEdit.text = null
                widgetDialog.show()
            }
            else widgetDialog.dismiss()
        })
    }

    private fun setWidgetSharedPreference(jsonArray: JSONArray) {
        val mSharedPreference
                = getSharedPreferences("widget", Context.MODE_PRIVATE)
        val editor = mSharedPreference.edit()
        editor.remove("data")
        editor.putString("data", jsonArray.toString())
        editor.apply()
    }

    private fun deleteDialog() {
        deleteDialog.setContentView(deleteDialogBinding.root)
        deleteDialogBinding.viewModel = viewModel
        viewModel.deleteDialogVisibility.observe(this, Observer {
            if (it) deleteDialog.show()
            else deleteDialog.dismiss()
        })
        viewModel.deleteWidget.observe(this, Observer {
            deleteDialogBinding.item = it
            viewModel.deleteDialogVisibility(true)
        })
    }

    private fun permissionChk() {
        //CALL_DIAL 로 바꾼 상태라 권한이 필요 없어짐 추후 사용할수 있다
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        }*/
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*when (requestCode) {
            1 -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                this.toast("동의하신 권한은 위젯 사용 시 사용됩니다.")
            } else {
                finish()
            }
        }*/
    }
}

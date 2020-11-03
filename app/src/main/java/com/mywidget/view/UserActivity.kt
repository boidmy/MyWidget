package com.mywidget.view

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MyAppWidget
import com.mywidget.R
import com.mywidget.adapter.UserAdapter
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import com.mywidget.databinding.ActivityUserBinding
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.*

class UserActivity : BaseActivity<UserViewModel, ActivityUserBinding>() {
    private var mAdapter: UserAdapter? = null
    private var userDb: UserDB? = null

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
        userDb = UserDB.getInstance(this)
        binding.viewModel = viewModel
        viewModel.userDB = userDb
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
        dimVisiblity(true)
        val popupView:View = layoutInflater.inflate(R.layout.main_phone_dialog, null)
        val popupWindow = PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        popupView.confirm_btn.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("아싸~")
                .setMessage("입력됐어요!")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    // 확인시 처리 로직
                    insertUser(popupView)
                    Toast.makeText(this, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    popupView.name_add.text = null
                    popupView.number_add.text = null
                    popupView.visibility = View.GONE
                    dimVisiblity(false)

                }
                .setNegativeButton(
                    android.R.string.no
                ) { _, _ ->
                    // 취소시 처리 로직
                    Toast.makeText(this, "취소했대요", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
        popupWindow.setOnDismissListener {
            dimVisiblity(false)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    private fun insertUser(v: View) {
        viewModel.insertUser(v.name_add.text.toString(), v.number_add.text.toString())
        //MainApplication.widgetBroad()
        intent = Intent(this, MyAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        this.sendBroadcast(intent)
    }

    private fun dimVisiblity(flag: Boolean) {
        if (flag) {
            dim_layout.visibility = View.VISIBLE
        } else {
            dim_layout.visibility = View.GONE
        }
    }
}

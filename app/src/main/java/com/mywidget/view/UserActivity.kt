package com.mywidget.view

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mywidget.MyAppWidget
import com.mywidget.R
import com.mywidget.adapter.UserAdapter
import com.mywidget.data.model.UserListData
import com.mywidget.data.room.User
import com.mywidget.data.room.UserDB
import com.mywidget.viewModel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.*

class UserActivity : AppCompatActivity() {
    private var db: SQLiteDatabase? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: UserAdapter? = null
    @SuppressLint("WrongConstant")
    private val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private var userDb: UserDB? = null
    private var viewModel: UserViewModel? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)

        db = this.openOrCreateDatabase("widgetDb", Context.MODE_PRIVATE, null)
        userDb = UserDB.getInstance(this)

        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        viewModel?.userDB = userDb

        init()

        add_txt.setOnClickListener(onClickListener)
    }

    private fun init() {

        mRecyclerView = findViewById(R.id.user_rv)
        mAdapter = UserAdapter(this, viewModel)

        mRecyclerView?.layoutManager = mLayoutManager
        mRecyclerView?.adapter = mAdapter

        viewModel?.data?.observe(this, Observer {
            mAdapter?.setData(it)
        })

        selectDb()
    }

    private fun selectDb() {
        Thread(Runnable {
            viewModel?.selectUser()
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
                .setMessage("♥입력됐대용♥")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    // 확인시 처리 로직
                    widgetAdd(popupView)
                    Toast.makeText(this, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    popupView.name_add.text = null
                    popupView.number_add.text = null
                    popupView.visibility = View.GONE
                    dimVisiblity(false)

                }
                .setNegativeButton(
                    android.R.string.no
                ) { dialog, whichButton ->
                    // 취소시 처리 로직
                    Toast.makeText(this, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
        popupWindow.setOnDismissListener {
            dimVisiblity(false)
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    fun widgetAdd(v: View) {
        val name: String = v.name_add.text.toString()
        val number: String = v.number_add.text.toString()

        Thread(Runnable {
            viewModel?.insertUser(name, number)
        }).start()

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

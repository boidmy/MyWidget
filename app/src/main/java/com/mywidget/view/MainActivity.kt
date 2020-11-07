package com.mywidget.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mywidget.*
import com.mywidget.adapter.TabPagerAdapter
import com.mywidget.databinding.DrawerlayoutMainBinding
import com.mywidget.lmemo.view.LMemoActivity
import com.mywidget.login.view.LoginGoogle
import com.mywidget.viewModel.MainFragmentViewModel
import com.mywidget.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.main_loveday_dialog.view.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.confirm_btn
import kotlinx.android.synthetic.main.memo_dialog.view.*
import java.util.*


class MainActivity : BaseActivity<MainFragmentViewModel, DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    private var alertDialog: AlertDialog.Builder? = null
    private lateinit var alert: AlertDialog
    private var mSharedPreference = MainApplication.INSTANSE.mSharedPreference
    private var editor = MainApplication.INSTANSE.editor

    private var backPressAppFinish: BackPressAppFinish? = null
    private var mTabPagerAdapter: TabPagerAdapter? = null
    private var memo_dialog: View? = null
    private var loveday_dialog: View? = null
    private var tabPosition: Int? = 0

    private lateinit var database: DatabaseReference

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun getViewModel(): Class<MainFragmentViewModel> {
        return MainFragmentViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        alertDialog = AlertDialog.Builder(this)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
        }

        database = FirebaseDatabase.getInstance().reference
        backPressAppFinish = BackPressAppFinish(this)

        addWidget()
        leftMenu()
        tabInit()
        googleLogin()

        floating_btn.setOnClickListener(onClickFloating)
    }

    private fun googleLogin() {
        val userAct = GoogleSignIn.getLastSignedInAccount(this)
        //fcmtest("")
    }

    private fun dimVisiblity(flag: Boolean) {
        if (flag) dim_layout.visibility = View.VISIBLE
        else dim_layout.visibility = View.GONE
    }

    private fun memoAdd(v: View?) {
        viewModel.insertMemo(v?.memo_txt?.text.toString(), v?.date_txt?.tag.toString())
    }

    private fun loveDayAdd(v: View?) {
        viewModel.addLoveDay(v?.day_add?.tag.toString())
    }

    private fun tabInit() {
        binding.mainContainer.mainTab.setupWithViewPager(binding.mainContainer.vpTab)
        binding.mainContainer.mainTab.setSelectedTabIndicatorColor(ContextCompat
            .getColor(this, R.color.white
        ))

        mTabPagerAdapter = TabPagerAdapter(supportFragmentManager)
        binding.mainContainer.vpTab.adapter = mTabPagerAdapter
        //mViewPager.adapter = mTabPagerAdapter

        binding.mainContainer.vpTab.addOnPageChangeListener(TabLayout
            .TabLayoutOnPageChangeListener(binding.mainContainer.mainTab))
        binding.mainContainer.mainTab.addOnTabSelectedListener(onTabSelectedListener)
    }

    override fun onBackPressed() {
        if(memo_dialog?.visibility == View.VISIBLE) {
            memo_dialog?.visibility = View.GONE
            dimVisiblity(false)
        }

        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        backPressAppFinish?.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            3000 -> {
                if("feel" == data?.getStringExtra("result")) {
                    val intent = Intent(this, LMemoActivity::class.java)
                    startActivity(intent)
                } else if("dday" == data?.getStringExtra("result")){
                    loveDday()
                }
            }
            4000 -> {
                val userAct = GoogleSignIn.getLastSignedInAccount(this)

                userAct?.let {
                    val name = it.givenName
                    val token = it.idToken

                    login_name.text = it.givenName
                }
            }
        }
    }

    fun fcmtest(token2: String?) {

        var test = sendTest()
        var token: String
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {itemcontainer ->
            token = itemcontainer.token
            //test.haha2(token,"")
        }
    }

    private fun addWidget() {
        MainApplication.widgetBroad()
        intent = Intent(this, MyAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        this.sendBroadcast(intent)
    }

    private fun leftMenu() {
        left_btn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                alertDialog?.setTitle("권한에 동의하셨네요?")
                    ?.setMessage("이젠 취소할 수 없습니다.")
                    ?.setPositiveButton("확인") { _, _ ->

                    }
                alert = alertDialog!!.create()
                alert.show()
            } else {
                alertDialog?.setTitle("권한을 동의하지 않으셨네요?")
                    ?.setMessage("어쩔수없이 앱을 종료합니다 ㅠㅠ")
                    ?.setCancelable(false)
                    ?.setPositiveButton("확인") { _, _ ->
                        finish()
                    }
                alert = alertDialog!!.create()
                alert.show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.left_delete -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            R.id.login_google -> {
                val intent = Intent(this, LoginGoogle::class.java)
                startActivityForResult(intent, 4000)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val memoCalOnClickListener = View.OnClickListener {
        val c = Calendar.getInstance()

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            memo_dialog?.date_txt?.text = year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString()
            memo_dialog?.date_txt?.tag = year.toString()+String.format("%02d", monthOfYear+1)+dayOfMonth.toString()

        }, CalendarUtil.getYear(c), CalendarUtil.getMonth(c), CalendarUtil.getNowdate(c))

        dpd.show()
    }

    private val loveDayCalOnClickListener = View.OnClickListener {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            loveday_dialog?.day_add?.text = year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString()
            loveday_dialog?.day_add?.tag = year.toString()+String.format("%02d", monthOfYear+1)+dayOfMonth.toString()
        }, year, month, day)

        dpd.show()
    }

    private fun onClickLoveDay() {
        val intent = Intent(this, LoveDayPopupActivity::class.java)
        startActivityForResult(intent, 3000)
    }

    private fun loveDday() {
        loveday_dialog = layoutInflater.inflate(R.layout.main_loveday_dialog, null)
        val popupWindow = PopupWindow(
            loveday_dialog,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(loveday_dialog, Gravity.CENTER, 0, 0)
        dimVisiblity(true)
        loveday_dialog?.day_add?.setOnClickListener(loveDayCalOnClickListener)

        loveday_dialog?.confirm_btn?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("아싸~")
                .setMessage("♥입력됐대용♥")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("yes") { _, _ ->
                    // 확인시 처리 로직
                    loveDayAdd(loveday_dialog)
                    Toast.makeText(this, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    loveday_dialog?.day_add?.text = null
                    loveday_dialog?.visibility = View.GONE
                    dimVisiblity(false)
                }
                .setNegativeButton(
                    android.R.string.no
                ) { _, _ ->
                    // 취소시 처리 로직
                    Toast.makeText(this, "취소했대요ㅠㅠ.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        popupWindow.setOnDismissListener {
            dimVisiblity(false)
        }
    }

    private fun onClickMemo() {
        memo_dialog = layoutInflater.inflate(R.layout.memo_dialog, null)
        val popupWindow = PopupWindow(
            memo_dialog,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(memo_dialog, Gravity.CENTER, 0, 0)
        dimVisiblity(true)
        memo_dialog?.memo_txt?.requestFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        memo_dialog?.cal_btn?.setOnClickListener(memoCalOnClickListener)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR).toString()
        val month = (c.get(Calendar.MONTH) + 1).toString()
        val day = c.get(Calendar.DAY_OF_MONTH).toString()
        memo_dialog?.date_txt?.text = "$year-$month-$day"

        popupWindow.isOutsideTouchable = true
        memo_dialog?.confirm_btn?.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("아싸~")
                .setMessage("♥입력됐대용♥")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton("yes") { _, _ ->
                    // 확인시 처리 로직
                    memoAdd(memo_dialog)
                    Toast.makeText(this, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    memo_dialog?.memo_txt?.text = null
                    memo_dialog?.date_txt?.text = null
                    memo_dialog?.visibility = View.GONE
                    dimVisiblity(false)

                }
                .setNegativeButton(
                    android.R.string.no
                ) { _, _ ->
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

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            when (tab?.position) {
                0 -> {
                    tabPosition = tab.position
                    floating_btn.background = ContextCompat.getDrawable(applicationContext,
                        R.drawable.circle_blue
                    )
                }
                else -> {
                    tabPosition = tab?.position
                    floating_btn.background = ContextCompat.getDrawable(applicationContext,
                        R.drawable.circle_red
                    )
                }
            }
        }
        override fun onTabReselected(tab: TabLayout.Tab?) {}
        override fun onTabUnselected(tab: TabLayout.Tab?) {}
    }

    private val onClickFloating = View.OnClickListener {
        when(tabPosition) {
            0 -> onClickMemo()
            else -> onClickLoveDay()
        }
    }
}

package com.mywidget.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mywidget.*
import com.mywidget.adapter.TabPagerAdapter
import com.mywidget.databinding.DrawerlayoutMainBinding
import com.mywidget.databinding.MainLovedayDialogBinding
import com.mywidget.databinding.MemoDialogBinding
import com.mywidget.di.compoenet.MainActivityComponent
import com.mywidget.lmemo.view.LMemoActivity
import com.mywidget.login.view.LoginGoogle
import com.mywidget.viewModel.MainFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.main_loveday_dialog.view.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.confirm_btn
import kotlinx.android.synthetic.main.memo_dialog.view.*
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    private var mSharedPreference = MainApplication.INSTANSE.mSharedPreference
    private var editor = MainApplication.INSTANSE.editor
    private var tabPosition: Int? = 0
    lateinit var mainComponent: MainActivityComponent
    private lateinit var database: DatabaseReference
    @Inject lateinit var factory: ViewModelProvider.Factory

    @Inject lateinit var mTabPagerAdapter: TabPagerAdapter
    @Inject lateinit var backPressAppFinish: BackPressAppFinish
    val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)}

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainComponent = (application as MainApplication).getApplicationCompoenet()
            .mainActivityComponent().create(this)
        mainComponent.inject(this)
        binding.viewModel = viewModel
        database = FirebaseDatabase.getInstance().reference

        permissionChk()
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

    private fun tabInit() {
        binding.mainContainer.mainTab.setupWithViewPager(binding.mainContainer.vpTab)
        binding.mainContainer.vpTab.adapter = mTabPagerAdapter
        binding.mainContainer.vpTab.addOnPageChangeListener(TabLayout
            .TabLayoutOnPageChangeListener(binding.mainContainer.mainTab))
        binding.mainContainer.mainTab.addOnTabSelectedListener(onTabSelectedListener)
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        backPressAppFinish.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            3000 -> {
                if("dday" == data?.getStringExtra("result")){
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

    private fun permissionChk() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val alert: AlertDialog?
        val alertDialog = AlertDialog.Builder(this)
        when (requestCode) {
            1 -> if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                alertDialog.setTitle("권한에 동의하셨네요?")
                    ?.setMessage("이젠 취소할 수 없습니다.")
                    ?.setPositiveButton("확인") { _, _ ->

                    }
                alert = alertDialog.create()
                alert.show()
            } else {
                alertDialog.setTitle("권한을 동의하지 않으셨네요?")
                    ?.setMessage("어쩔수없이 앱을 종료합니다 ㅠㅠ")
                    ?.setCancelable(false)
                    ?.setPositiveButton("확인") { _, _ ->
                        finish()
                    }
                alert = alertDialog.create()
                alert.show()
            }
        }
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

    private fun onClickLoveDay() {
        val intent = Intent(this, LoveDayPopupActivity::class.java)
        startActivityForResult(intent, 3000)
    }

    private fun loveDday() {
        val dialog = Dialog(this)
        val loveDayBinding = MainLovedayDialogBinding.inflate(LayoutInflater.from(this))
        loveDayBinding.viewModel = viewModel
        dialog.setContentView(loveDayBinding.root)
        viewModel.dialogVisible.value = true
        dialog.show()
        viewModel.dialogVisible.observe(this, androidx.lifecycle.Observer {
            if(!it) dialog.dismiss()
        })
    }

    private fun onClickMemo() {
        val dialog = Dialog(this)
        val memoDialogBinding = MemoDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(memoDialogBinding.root)
        memoDialogBinding.viewModel = viewModel
        dialog.show()
        viewModel.dialogVisible.value = true
        viewModel.dialogVisible.observe(this, androidx.lifecycle.Observer {
            if(!it) {
                dialog.dismiss()
                Util.downKeyboard(this)
            }
        })

        memoDialogBinding.apply {
            memoTxt.requestFocus()
            Util.upKeyboard(this@MainActivity)
            val c = Calendar.getInstance()
            CalendarUtil.apply {
                date = "${getYear(c)}-${getMonth(c)+1}-${getNowdate(c)}"
            }
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

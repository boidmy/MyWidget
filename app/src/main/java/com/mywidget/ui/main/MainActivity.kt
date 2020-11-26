package com.mywidget.ui.main

import android.Manifest
import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.iid.FirebaseInstanceId
import com.mywidget.*
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.DrawerlayoutMainBinding
import com.mywidget.databinding.MainLovedayDialogBinding
import com.mywidget.databinding.MemoDialogBinding
import com.mywidget.login.view.LoginGoogle
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.loveday.LoveDayPopupActivity
import com.mywidget.ui.widgetlist.WidgetListActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_title.*
import util.CalendarUtil
import util.Util
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    private var mSharedPreference = MainApplication.INSTANSE.mSharedPreference
    private var editor = MainApplication.INSTANSE.editor
    private var tabPosition: Int? = 0
    @Inject lateinit var mTabPagerAdapter: MainTabPagerAdapter
    @Inject lateinit var backPressAppFinish: BackPressAppFinish
    @Inject lateinit var factory: ViewModelProvider.Factory

    val viewModel: MainFragmentViewModel by lazy {
        ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)}

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        permissionChk()
        addWidget()
        leftMenu()
        tabInit()
        googleLogin()
        memoDialogBind()
        loveDayBind()

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
                    ?.setPositiveButton("확인") { _, _ -> }
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
                val intent = Intent(this, WidgetListActivity::class.java)
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

    private val loveDayDialogBinding by lazy { MainLovedayDialogBinding.inflate(LayoutInflater.from(this)) }
    private var lovedayDialog: Dialog? = null
    private fun loveDayBind() {
        lovedayDialog = Dialog(this)
        lovedayDialog?.setContentView(loveDayDialogBinding.root)
    }

    private fun loveDday() {
        loveDayDialogBinding.viewModel = viewModel
        viewModel.dialogVisible.value = true
        lovedayDialog?.show()
        viewModel.dialogVisible.observe(this, androidx.lifecycle.Observer {
            if(!it) lovedayDialog?.dismiss()
        })
    }

    private val memoDialogBinding by lazy { MemoDialogBinding.inflate(LayoutInflater.from(this)) }
    private var memoDialog: Dialog? = null
    private fun memoDialogBind() {
        memoDialog = Dialog(this)
        memoDialog?.setContentView(memoDialogBinding.root)
        memoDialogBinding.viewModel = viewModel
    }

    private fun onClickMemo() {
        memoDialog?.show()
        viewModel.dialogVisible.value = true
        viewModel.dialogVisible.observe(this, androidx.lifecycle.Observer {
            if(!it) {
                memoDialog?.dismiss()
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

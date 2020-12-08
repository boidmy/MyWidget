package com.mywidget.ui.main

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.mywidget.*
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.DrawerlayoutMainBinding
import com.mywidget.databinding.MainLovedayDialogBinding
import com.mywidget.databinding.MemoDialogBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.loveday.LoveDayPopupActivity
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import com.mywidget.ui.widgetlist.WidgetListActivity
import kotlinx.android.synthetic.main.activity_main.*
import util.CalendarUtil
import util.Util
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    private var tabPosition: Int? = 0
    @Inject lateinit var mTabPagerAdapter: MainTabPagerAdapter
    @Inject lateinit var backPressAppFinish: BackPressAppFinish
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainFragmentViewModel> { factory }

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        leftMenu()
        tabInit()
        loginCheck()
        memoDialogBind()
        loveDayBind()

        floating_btn.setOnClickListener(onClickFloating)
    }

    private fun loginCheck() {
        val email = loginEmail()
        if(email.isNotEmpty()) {
            viewModel.userEmail.value = email
            binding.navView.menu.getItem(1).title = "로그아웃"
        }
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
                val response = IdpResponse.fromResultIntent(data)
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.email?.let {
                    Util.toast(this, it+"님 환영합니다!")
                    viewModel.userEmail.value = user.email?:""
                    loginTxt("로그아웃")
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

    private fun leftMenu() {
        binding.mainContainer.titleContainer.leftMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun loginTxt(text: String) {
        binding.navView.menu.getItem(1).title = text
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.widget_phone_add -> {
                val intent = Intent(this, WidgetListActivity::class.java)
                startActivity(intent)
            }
            R.id.login -> {
                if(binding.navView.menu.getItem(1).title.toString() == "로그인") {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivityForResult(intent, 4000)
                } else {
                    Firebase.auth.signOut()
                    viewModel.userEmail.value = ""
                    loginTxt("로그인")
                }
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
        loveDayDialogBinding.viewModel = viewModel
    }

    private fun loveDday() {
        viewModel.loveDayDialogVisible.value = true
        viewModel.loveDayDialogVisible.observe(this, androidx.lifecycle.Observer {
            if(it) lovedayDialog?.show()
            else lovedayDialog?.dismiss()
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
        viewModel.memoDialogVisible.observe(this, androidx.lifecycle.Observer {
            if(it) {
                memoDialog?.show()
            } else {
                memoDialog?.dismiss()
                Util.downKeyboard(this)
            }
        })
        viewModel.memoDialogVisible.value = true

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

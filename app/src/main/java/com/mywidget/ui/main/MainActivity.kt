package com.mywidget.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mywidget.*
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.DrawerlayoutMainBinding
import com.mywidget.databinding.MainLovedayDialogBinding
import com.mywidget.databinding.MemoDialogBinding
import com.mywidget.databinding.NavHeaderMainBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.loveday.FloatingPopupActivity
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import com.mywidget.ui.widgetlist.WidgetListActivity
import util.CalendarUtil
import util.CalendarUtil.getToday
import util.Util
import util.Util.toast
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity<DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var mTabPagerAdapter: MainTabPagerAdapter
    @Inject lateinit var backPressAppFinish: BackPressAppFinish
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<MainFragmentViewModel> { factory }
    private val loveDayDialogBinding by lazy {
        MainLovedayDialogBinding.inflate(LayoutInflater.from(this))
    }
    private val lovedayDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }
    private val memoDialogBinding by lazy {
        MemoDialogBinding.inflate(LayoutInflater.from(this))
    }
    private val memoDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        loginCheck()
        leftMenu()
        tabInit()
        memoDialogBind()
        loveDayBind()
    }

    private fun loginCheck() {
        val email = loginEmail()
        if(email.isNotEmpty()) {
            viewModel.login(email)
            binding.navView.menu.getItem(1).title = "로그아웃"
        }
    }

    private fun tabInit() {
        binding.mainContainer.mainTab.setupWithViewPager(binding.mainContainer.vpTab)
        binding.mainContainer.vpTab.adapter = mTabPagerAdapter
        binding.mainContainer.vpTab.addOnPageChangeListener(TabLayout
            .TabLayoutOnPageChangeListener(binding.mainContainer.mainTab))
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
                if("memo" == data?.getStringExtra("result")) {
                    openMemoDialog()
                } else if("dDay" == data?.getStringExtra("result")) {
                    openLoveDayDialog()
                }
            }
            4000 -> {
                val user = FirebaseAuth.getInstance().currentUser
                user?.email?.let {
                    this.toast(it+"님 환영합니다!")
                    viewModel.login(it)
                    loginTxt("로그아웃")
                }
            }
        }
    }

    private fun leftMenu() {
        binding.mainContainer.titleContainer.leftMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        val drawerHeaderBinding: NavHeaderMainBinding = DataBindingUtil.inflate(layoutInflater
            , R.layout.nav_header_main, binding.navView, false)
        drawerHeaderBinding.lifecycleOwner = this
        drawerHeaderBinding.viewModel = viewModel
        binding.navView.addHeaderView(drawerHeaderBinding.root)
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun loginTxt(text: String) {
        binding.navView.menu.getItem(1).title = text
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
                    viewModel.logout(loginEmail())
                    Firebase.auth.signOut()
                    viewModel.userEmail.value = ""
                    loginTxt("로그인")
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loveDayBind() {
        lovedayDialog.setContentView(loveDayDialogBinding.root)
        loveDayDialogBinding.viewModel = viewModel
        viewModel.loveDayDialogVisible.observe(this, androidx.lifecycle.Observer {
            if(it) lovedayDialog.show()
            else lovedayDialog.dismiss()
        })
    }

    private fun openLoveDayDialog() {
        viewModel.loveDayDialogVisible(true)
        loveDayDialogBinding.apply {
            date = getToday()
        }
    }

    private fun memoDialogBind() {
        memoDialog.setContentView(memoDialogBinding.root)
        memoDialogBinding.viewModel = viewModel
        viewModel.memoDialogVisibility.observe(this, androidx.lifecycle.Observer {
            if(it) { memoDialog.show() }
            else {
                memoDialog.dismiss()
                Util.downKeyboard(this)
            }
        })
    }

    private fun openMemoDialog() {
        viewModel.memoDialogVisibility(true)
        memoDialogBinding.apply {
            memoEdit.text = null
            date = getToday()
        }
    }

    fun onClickFloating(v: View) {
        val intent = Intent(this, FloatingPopupActivity::class.java)
        startActivityForResult(intent, 3000)
    }
}

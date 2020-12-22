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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mywidget.*
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.*
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.friend.FriendActivity
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.loveday.FloatingPopupActivity
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import com.mywidget.ui.widgetlist.WidgetListActivity
import util.CalendarUtil.getToday
import util.Util
import util.Util.toast
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
    private val favoritesDialogBinding by lazy {
        MainFavoritesDialogBinding.inflate(LayoutInflater.from(this))
    }
    private val favoritesDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.drawerlayout_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        loginCheck()
        leftMenu()
        tabInit()
        memoDialogBind()
        loveDayDialogBind()
        favoritesDialogBind()
    }

    private fun loginCheck() {
        val email = loginEmail()
        viewModel.myIdReset()
        if(email.isNotEmpty()) {
            viewModel.myId(email)
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
                when {
                    "memo" == data?.getStringExtra("result") -> openMemoDialog()
                    "dDay" == data?.getStringExtra("result") -> openLoveDayDialog()
                    "condition" == data?.getStringExtra("result") -> openFavoritesDialog()
                }
            }
            4000 -> {
                with(loginEmail()) {
                    if(this.isEmpty()) return
                    this@MainActivity.toast(this+"님 환영합니다!")
                    viewModel.myId(this)
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
                if (binding.navView.menu.getItem(1).title.toString() == "로그인") {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivityForResult(intent, 4000)
                } else {
                    viewModel.logout(loginEmail())
                    Firebase.auth.signOut()
                    loginTxt("로그인")
                }
            }
            R.id.friendAdd -> {
                if (loginChkToast()) {
                    val intent = Intent(this, FriendActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loveDayDialogBind() {
        lovedayDialog.setContentView(loveDayDialogBinding.root)
        loveDayDialogBinding.viewModel = viewModel
        viewModel.loveDayDialogVisibility.observe(this, androidx.lifecycle.Observer {
            if(it) lovedayDialog.show()
            else lovedayDialog.dismiss()
        })
    }

    private fun openLoveDayDialog() {
        viewModel.loveDayDialogVisibility(true)
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
            memoEdit.isFocusable = true
            date = getToday()
        }
    }

    private fun favoritesDialogBind() {
        favoritesDialog.setContentView(favoritesDialogBinding.root)
        favoritesDialogBinding.viewModel = viewModel
        viewModel.favoritesExistence()
        viewModel.favoritesDialogVisibility.observe(this, Observer {
            if(it) favoritesDialog.show()
        })
        viewModel.favoritesExistence.observe(this, Observer {
            if(!it) this.toast("즐겨찾기 한 친구가 없어요!")
            else favoritesDialog.dismiss()
        })
    }

    private fun openFavoritesDialog() {
        viewModel.favoritesDialogVisibility(true)
        favoritesDialogBinding.apply {
            conditionTxtArea.text = null
            conditionTxtArea.isFocusable = true
        }
    }

    fun onClickFloating(v: View) {
        val intent = Intent(this, FloatingPopupActivity::class.java)
        startActivityForResult(intent, 3000)
    }
}

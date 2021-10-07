package com.mywidget.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mywidget.R
import com.mywidget.common.BackPressAppFinish
import com.mywidget.databinding.*
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.ui.friend.FriendActivity
import com.mywidget.ui.login.LoginActivity
import com.mywidget.ui.loveday.FloatingPopupActivity
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import com.mywidget.ui.mypage.MyPageActivity
import com.mywidget.ui.widgetlist.WidgetListActivity
import util.CalendarUtil.getToday
import util.Util
import util.Util.click
import util.Util.toast
import javax.inject.Inject
import javax.inject.Named


class MainActivity : BaseActivity<DrawerlayoutMainBinding>()
    , NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var mTabPagerAdapter: MainTabPagerAdapter
    @Inject lateinit var backPressAppFinish: BackPressAppFinish
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var loveDayDialogBinding: MainLovedayDialogBinding
    @Inject lateinit var loveDayDialog: Dialog
    @Inject lateinit var memoDialogBinding: MemoDialogBinding
    @Inject lateinit var memoDialog: Dialog
    @Inject lateinit var favoritesDialogBinding: MainFavoritesDialogBinding
    @Inject lateinit var favoritesDialog: Dialog
    @Inject @Named("open") lateinit var floatingAnimationOpen: Animation
    @Inject @Named("close") lateinit var floatingAnimationClose: Animation

    private val viewModel by viewModels<MainFragmentViewModel> { factory }

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
        chatInPush()
    }

    private fun loginCheck() {
        val email = loginEmail()
        if(email.isNotEmpty()) {
            viewModel.myId(email)
            binding.navView.menu.getItem(1).title = "로그아웃"
        }
    }

    private fun leftMenu() {
        binding.mainContainer.titleContainer.leftMenu.click {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        val drawerHeaderBinding: NavHeaderMainBinding = DataBindingUtil.inflate(layoutInflater
            , R.layout.nav_header_main, binding.navView, false)
        drawerHeaderBinding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = viewModel
            binding.navView.addHeaderView(root)
        }
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun tabInit() {
        binding.mainContainer.apply {
            mainTab.setupWithViewPager(binding.mainContainer.vpTab)
            vpTab.adapter = mTabPagerAdapter
            vpTab.addOnPageChangeListener(TabLayout
                .TabLayoutOnPageChangeListener(binding.mainContainer.mainTab))
        }
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
                when (data?.getStringExtra("result")) {
                    "memo" -> openMemoDialog()
                    "dDay" -> openLoveDayDialog()
                    "condition" -> openFavoritesDialog()
                    "chat" -> {
                        val intent = Intent(this, ChatRoomActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            4000 -> {
                with(loginEmail()) {
                    if(isEmpty()) return
                    this@MainActivity.toast(this+"님 환영합니다!")
                    viewModel.myId(this)
                    loginTxt("로그아웃")
                }
            }
        }
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
            R.id.myPage -> {
                if (loginChkToast()) {
                    val intent = Intent(this, MyPageActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loveDayDialogBind() {
        loveDayDialog.setContentView(loveDayDialogBinding.root)
        loveDayDialogBinding.viewModel = viewModel
        viewModel.loveDayDialogVisibility.observe(this, androidx.lifecycle.Observer {
            if(it) loveDayDialog.show()
            else loveDayDialog.dismiss()
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
            }
        })
    }

    private fun openMemoDialog() {
        viewModel.memoDialogVisibility(true)
        memoDialogBinding.apply {
            memoEdit.text = null
            memoEdit.isFocusable = true
            calendarTxtArea.text = null
            date = getToday()
        }
    }

    private fun favoritesDialogBind() {
        favoritesDialog.setContentView(favoritesDialogBinding.root)
        favoritesDialogBinding.viewModel = viewModel
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

    private fun chatInPush() {
        val intent = intent
        val extras = intent.extras
        val chatInformation = extras?.getString(getString(R.string.runChat))
        chatInformation?.let {
            val intent = Intent(this, ChatRoomActivity::class.java)
            val chatRoomData: Array<String> = it.split("&&".toRegex()).toTypedArray()
            intent.putExtra(getString(R.string.runChat), chatRoomData)
            startActivity(intent)
        }
    }
}

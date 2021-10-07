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
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mywidget.R
import com.mywidget.common.BackPressAppFinish
import com.mywidget.data.*
import com.mywidget.data.Constants.Companion.REQUEST_CODE_FLOATING
import com.mywidget.data.Constants.Companion.REQUEST_CODE_LOGIN
import com.mywidget.databinding.*
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.chatroom.ChatRoomActivity
import com.mywidget.ui.main.fragment.MainTabPagerAdapter
import util.CalendarUtil.getToday
import util.LandingRouter.move
import util.Util.click
import util.Util.toast
import util.observe
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

        loginCheck()
        bind()
        bindLeftMenu()
        memoDialogBind()
        loveDayDialogBind()
        favoritesDialogBind()
        chatInPush()
    }

    private fun loginCheck() {
        loginEmail().run {
            if (isNotEmpty()) {
                viewModel.myId(this)
                binding.navView.menu.getItem(1).title = getString(R.string.logout)
            }
        }
    }

    private fun bind() {
        binding.apply {
            vm = viewModel
        }.mainContainer.apply {
            mainTab.setupWithViewPager(vpTab)
            vpTab.adapter = mTabPagerAdapter
            vpTab.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mainTab))
        }
        setObserve()
    }

    private fun setObserve() {
        with(viewModel) {
            observe(loveDayDialogVisibility, ::loveDayDialogShow)
            observe(memoDialogVisibility, ::memoDialogShow)
            observe(favoritesDialogVisibility, ::favoritesDialogShow)
            observe(favoritesExistence, ::favoritesExistenceChk)
        }
    }

    private fun bindLeftMenu() {
        binding.mainContainer.titleContainer.leftMenu.click {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        inflateLeftMenu().apply {
            lifecycleOwner = this@MainActivity
            viewModel = viewModel
            binding.navView.addHeaderView(root)
        }

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun inflateLeftMenu(): NavHeaderMainBinding = DataBindingUtil.inflate(layoutInflater
        , R.layout.nav_header_main, binding.navView, false)

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        backPressAppFinish.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_FLOATING -> {
                when (data?.getStringExtra(RESULT)) {
                    MEMO -> openMemoDialog()
                    CONDITION -> openFavoritesDialog()
                    D_DAY -> openLoveDayDialog()
                    CHAT -> move(RouterEvent(type = Landing.CHAT_ROOM))
                }
            }
            REQUEST_CODE_LOGIN -> {
                with(loginEmail()) {
                    if (isEmpty()) return
                    this@MainActivity.toast(String.format(this@MainActivity.getString(R.string.welcomeUser), this))
                    viewModel.myId(this)
                    loginTxt(getString(R.string.logout))
                }
            }
        }
    }

    private fun loginTxt(text: String) {
        binding.navView.menu.getItem(1).title = text
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.widget_phone_add -> move(RouterEvent(type = Landing.WIDGET))
            R.id.login -> {
                if (loginEmail().isEmpty()) {
                    move(RouterEvent(type = Landing.LOGIN))
                } else {
                    viewModel.logout(loginEmail())
                    Firebase.auth.signOut()
                    loginTxt(getString(R.string.login))
                }
            }
            R.id.friendAdd -> if (loginChkToast()) move(RouterEvent(type = Landing.FRIEND))
            R.id.myPage -> if (loginChkToast()) move(RouterEvent(type = Landing.MYPAGE))
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loveDayDialogBind() {
        loveDayDialogBinding.apply {
            loveDayDialog.setContentView(root)
            vm = viewModel
        }
    }

    private fun openLoveDayDialog() {
        viewModel.loveDayDialogVisibility(true)
        loveDayDialogBinding.date = getToday()
    }

    private fun memoDialogBind() {
        memoDialogBinding.apply {
            memoDialog.setContentView(root)
            vm = viewModel
        }
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
        favoritesDialogBinding.apply {
            favoritesDialog.setContentView(root)
            vm = viewModel
        }
    }

    private fun openFavoritesDialog() {
        viewModel.favoritesDialogVisibility(true)
        favoritesDialogBinding.apply {
            conditionTxtArea.text = null
            conditionTxtArea.isFocusable = true
        }
    }

    fun onClickFloating(v: View) {
        move(RouterEvent(type = Landing.FLOATING))
    }

    private fun chatInPush() {
        val chatInformation = intent.extras?.getString(getString(R.string.runChat))
        chatInformation?.let {
            val intent = Intent(this, ChatRoomActivity::class.java)
            val chatRoomData: Array<String> = it.split("&&".toRegex()).toTypedArray()
            intent.putExtra(getString(R.string.runChat), chatRoomData)
            startActivity(intent)
        }
    }

    private fun loveDayDialogShow(flag: Boolean) {
        if (flag) loveDayDialog.show()
        else loveDayDialog.dismiss()
    }

    private fun memoDialogShow(flag: Boolean) {
        if (flag) memoDialog.show()
        else memoDialog.dismiss()
    }

    private fun favoritesDialogShow(flag: Boolean) {
        if (flag) favoritesDialog.show()
    }

    private fun favoritesExistenceChk(flag: Boolean) {
        if (flag) favoritesDialog.dismiss()
        else this.toast("즐겨찾기 한 친구가 없어요!")
    }
}

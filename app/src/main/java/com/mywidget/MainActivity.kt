package com.mywidget

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mywidget.adapter.TabPagerAdapter
import com.mywidget.lmemo.view.LMemoActivity
import com.mywidget.login.view.LoginGoogle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_title.*
import kotlinx.android.synthetic.main.main_loveday_dialog.view.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.*
import kotlinx.android.synthetic.main.main_phone_dialog.view.confirm_btn
import kotlinx.android.synthetic.main.memo_dialog.view.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var alertDialog: AlertDialog.Builder? = null
    private lateinit var alert: AlertDialog
    private var mSharedPreference = MainApplication.INSTANSE.mSharedPreference
    private var editor = MainApplication.INSTANSE.editor
    private var db = MainApplication.INSTANSE.db

    private val user_table = "USER"
    private val menu_table = "MENU"
    private val memo_table = "MEMO"
    private val loveday_table = "LOVEDAY"

    private var backPressAppFinish: BackPressAppFinish? = null
    private var mTabPagerAdapter: TabPagerAdapter? = null
    private var memo_dialog: View? = null
    private var loveday_dialog: View? = null
    var drawerLayout: DrawerLayout? = null
    private var tabPosition: Int? = 0

    private lateinit var database: DatabaseReference


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawerlayout_main)

        db?.execSQL("CREATE TABLE IF NOT EXISTS " + user_table + " (name VARCHAR(20), phone VARCHAR(20))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS " + menu_table + " (name VARCHAR(20))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS " + memo_table + " (memo VARCHAR(40), date VARCHAR(20))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS " + loveday_table + " (date VARCHAR(20))")

        alertDialog = AlertDialog.Builder(this)
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        }

        database = FirebaseDatabase.getInstance().reference

        backPressAppFinish = BackPressAppFinish(this)

        init()
        leftMenu()
        tabInit()

        add_txt.setOnClickListener {
            if (tabPosition == 0) {
                onClickMemo()
            } else {
                onClickLoveDay()
            }
        }
    }

    private fun dimVisiblity(flag: Boolean) {
        if (flag) {
            dim_layout.visibility = View.VISIBLE
        } else {
            dim_layout.visibility = View.GONE
        }
    }

    private fun memoAdd(v: View?) {
        val name: String = v?.memo_txt?.text.toString()
        val date: String = v?.date_txt?.tag.toString()

        //db?.execSQL("INSERT INTO$table(name, phone) Values ('$name', '$number')")
        db?.execSQL("INSERT INTO " + memo_table + "(memo, date)" + "Values ('$name', '$date')")
        mTabPagerAdapter?.itemNotify()
    }

    private fun loveDayAdd(v: View?) {
        val day: String = v?.day_add?.tag.toString()

        val cursor = db?.rawQuery("SELECT * FROM " + loveday_table, null)
        try {
            if (cursor?.moveToFirst() != null) {
                db?.execSQL("delete from " + loveday_table)
            }
        } catch (e: Exception) {

        }

        //db?.execSQL("INSERT INTO$table(name, phone) Values ('$name', '$number')")
        db?.execSQL("INSERT INTO " + loveday_table + "(date)" + "Values ('$day')")
        mTabPagerAdapter?.notifyDataSetChanged()
    }

    private fun tabInit() {
        val mTabLayout: TabLayout = findViewById(R.id.main_tab)
        val mViewPager: ViewPager = findViewById(R.id.vp_tab)
        mTabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.tab_gray), ContextCompat.getColor(this, R.color.white))
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white))

        mTabPagerAdapter = TabPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mTabPagerAdapter

        mTabPagerAdapter?.notifyDataSetChanged()

        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        tabPosition = tab.position
                        add_txt.background = ContextCompat.getDrawable(applicationContext, R.drawable.circle_blue)
                    }
                    else -> {
                        tabPosition = tab.position
                        add_txt.background = ContextCompat.getDrawable(applicationContext, R.drawable.circle_red)
                    }
                }

                mViewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onBackPressed() {
        if(memo_dialog?.visibility == View.VISIBLE) {
            memo_dialog?.visibility = View.GONE
            dimVisiblity(false)
        }

        if(drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            drawerLayout?.closeDrawer(GravityCompat.START)
            return
        }

        backPressAppFinish?.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            3000 -> {
                if("feel" == data?.getStringExtra("result")) {
                    val intent = Intent(this, LMemoActivity::class.java)
                    startActivity(intent)
                } else if("dday" == data?.getStringExtra("result")){
                    loveDday()
                }
            }
        }
    }

    fun widgetAdd(v: View) {
        val name: String = v.name_add.text.toString()
        val number: String = v.number_add.text.toString()

        //db?.execSQL("INSERT INTO$table(name, phone) Values ('$name', '$number')")
        db?.execSQL("INSERT INTO " + user_table + "(name, phone)" + "Values ('$name', '$number')")

        /*var arrayList = arrayListOf<String>()
        arrayList.add(name)
        arrayList.add(number)
        var set = HashSet<String>()
        set.addAll(arrayList)

        editor = mSharedPreference?.edit()

        editor?.putStringSet("user", set)*/

        init()
    }

    private fun init() {
        MainApplication.widgetBroad()
        intent = Intent(this, MyAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        this.sendBroadcast(intent)

        val alpha: Drawable = loveday_bg.background
        alpha.alpha = 20

        loveday_bg.visibility = View.VISIBLE
    }

    private fun leftMenu() {
        val nav_view: NavigationView = findViewById(R.id.nav_view)

        drawerLayout = findViewById(R.id.drawer_layout)

        left_btn.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }

        nav_view.setNavigationItemSelectedListener(this)
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

    /*private fun menuAdd(v: View) {
        val name: String = v.menu_add.text.toString()

        //db?.execSQL("INSERT INTO$table(name, phone) Values ('$name', '$number')")
        db?.execSQL("INSERT INTO " + menu_table + "(name)" + "Values ('$name')")
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.left_delete -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            R.id.widget_phone_add -> {

            }
            R.id.login_google -> {
                val intent = Intent(this, LoginGoogle::class.java)
                startActivity(intent)
            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    private val memoCalOnClickListener = View.OnClickListener {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            memo_dialog?.date_txt?.text = year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString()
            memo_dialog?.date_txt?.tag = year.toString()+String.format("%02d", monthOfYear+1)+dayOfMonth.toString()

        }, year, month, day)

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
        var intent = Intent(this, LoveDayPopupActivity::class.java)
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
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
                    // 확인시 처리 로직
                    loveDayAdd(loveday_dialog)
                    Toast.makeText(this, "저장했대요!!", Toast.LENGTH_SHORT).show()
                    loveday_dialog?.day_add?.text = null
                    loveday_dialog?.visibility = View.GONE
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
                .setPositiveButton(android.R.string.yes) { dialog, whichButton ->
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

    fun hahahoho() {

    }
}

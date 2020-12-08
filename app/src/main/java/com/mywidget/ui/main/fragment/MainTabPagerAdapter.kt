package com.mywidget.ui.main.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.mywidget.ui.main.fragment.FragmentLoveDay
import com.mywidget.ui.main.fragment.FragmentMemo

class MainTabPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var mFragmentMemo: FragmentMemo? = null
    private var mFragmentLoveDay: FragmentLoveDay? = null

    override fun getItem(position: Int): Fragment {
        return if(position == 0) {
            mFragmentMemo = FragmentMemo()
            mFragmentMemo as FragmentMemo
        } else {
            mFragmentLoveDay = FragmentLoveDay()
            mFragmentLoveDay as FragmentLoveDay
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(position == 0)
            "D-Day"
        else
            "LoveDays"
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` as Fragment == mFragmentLoveDay) {
            return PagerAdapter.POSITION_NONE
        } else {
            return super.getItemPosition(`object`)
        }
    }

}
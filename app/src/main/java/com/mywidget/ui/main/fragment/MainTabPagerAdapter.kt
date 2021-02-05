package com.mywidget.ui.main.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import javax.inject.Inject

class MainTabPagerAdapter @Inject constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var mFragmentMemo = FragmentMemo()
    private var mFragmentLoveDay = FragmentLoveDay()

    override fun getItem(position: Int): Fragment {
        return if(position == 0) {
            mFragmentMemo
        } else {
            mFragmentLoveDay
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if(position == 0)
            "D-Day"
        else
            "LoveDays"
    }

    override fun getItemPosition(`object`: Any): Int {
        return if (`object` as Fragment == mFragmentLoveDay) {
            PagerAdapter.POSITION_NONE
        } else {
            super.getItemPosition(`object`)
        }
    }

}
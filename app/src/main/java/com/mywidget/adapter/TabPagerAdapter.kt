package com.mywidget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.mywidget.fragment.FragmentLoveDay
import com.mywidget.fragment.FragmentMemo

class TabPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    var mFragmentMemo: FragmentMemo? = null
    var mFragmentLoveDay: FragmentLoveDay? = null

    override fun getItem(position: Int): Fragment? {
        if(position == 0) {
            mFragmentMemo = FragmentMemo.newInstance(position)
            return mFragmentMemo as FragmentMemo
        } else {
            mFragmentLoveDay = FragmentLoveDay.newInstance(position)
            return mFragmentLoveDay as FragmentLoveDay
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

    fun itemNotify() {
        mFragmentMemo?.notifyCall()
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` as Fragment == mFragmentLoveDay) {
            return PagerAdapter.POSITION_NONE
        } else {
            return super.getItemPosition(`object`)
        }
    }

}
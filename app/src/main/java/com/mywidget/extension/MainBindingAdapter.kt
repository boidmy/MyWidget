package com.mywidget.extension

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.tabs.TabLayout
import com.mywidget.R
import kotlinx.android.synthetic.main.activity_main.*

@BindingAdapter("tabSelect")
fun tabSelect(tabLayout: TabLayout, position: List<Int>) {
    /*tabLayout.addOnTabSelectedListener(
        object : TabLayout.OnTabSelectedListener {
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
    )*/
}
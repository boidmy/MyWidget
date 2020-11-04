package com.mywidget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.loveday_popup.*

class LoveDayPopupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.loveday_popup)

        loveday_feeling.setOnClickListener(clickListener)
        loveday_dday.setOnClickListener(clickListener)
        smart_talk_floating_popup_dim_layout.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener { v ->
        when (v.id){
            R.id.loveday_feeling -> {
                val intent = Intent()
                intent.putExtra("result", "feel")
                setResult(RESULT_OK, intent)
                finish()
            }
            R.id.loveday_dday -> {
                val intent = Intent()
                intent.putExtra("result", "dday")
                setResult(RESULT_OK, intent)
                finish()
            }
            R.id.smart_talk_floating_popup_dim_layout -> finish()
        }
    }
}
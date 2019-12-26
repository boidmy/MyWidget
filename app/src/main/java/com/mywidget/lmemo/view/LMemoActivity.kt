package com.mywidget.lmemo.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mywidget.R
import com.mywidget.common.StateMaintainer
import com.mywidget.lmemo.LmemoContract
import com.mywidget.lmemo.model.Lmodel
import com.mywidget.lmemo.presenter.Lpresenter
import kotlinx.android.synthetic.main.activity_love_memo.*

class LMemoActivity : AppCompatActivity(), View.OnClickListener, LmemoContract.View {

    private var mPresenter:Lpresenter? = null

    private val mStateMaintainer: StateMaintainer = StateMaintainer(fragmentManager, LMemoActivity::class.java.name)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_love_memo)

        confirm_btn.setOnClickListener(this)

        setupMVP()
    }

    private fun setupMVP() {
        if(mStateMaintainer.firstTimeIn()) {
            val presenter = Lpresenter(this)
            val lmodel = Lmodel(presenter)
            presenter.setModel(lmodel)
            mStateMaintainer.put(presenter)
            mStateMaintainer.put(lmodel)

            mPresenter = presenter
        } else {
            mPresenter = mStateMaintainer.get(Lpresenter::class.java.name)
            mPresenter?.setView(this)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View) {
        when (view.id) {
            R.id.confirm_btn -> {
                mPresenter?.clickMemoadd(name_edit.text.toString(), feel_edit.text.toString())
            }
        }
    }

    override fun notifyDataSetChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
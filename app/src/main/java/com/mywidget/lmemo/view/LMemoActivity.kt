package com.mywidget.lmemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mywidget.R
import com.mywidget.common.StateMaintainer
import com.mywidget.lmemo.LmemoContract
import com.mywidget.lmemo.model.Lmodel
import com.mywidget.lmemo.presenter.Lpresenter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_love_memo.*

class LMemoActivity : AppCompatActivity(), View.OnClickListener, LmemoContract.View {

    private var mPresenter:Lpresenter? = null

    private val mStateMaintainer: StateMaintainer = StateMaintainer(fragmentManager, LMemoActivity::class.java.name)


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_love_memo)

        confirm_btn.setOnClickListener(this)

        setupMVP()

        mPresenter?.selectNickName()


        var ee = Observable.create<String>{ha: ObservableEmitter<String> ->
            ha.onNext("방출하라삐리삐리")
        }

        val aa = PublishSubject.create<String>()
            .onNext("하하하하")

        var qq = Observable.create<View> {confirm_btn.setOnClickListener {

        }}
            .subscribe{

        }

        var ob2 = Observable.just(1.0)

        var ob = Observable.create{ emitter: ObservableEmitter<String> ->
            emitter.onNext("나다")

        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        var list = listOf(1,2,3)

        list.filter {
            it > 1
        }.map {
            it * 2
            Log.d("뭐뜨내ㅑ", it.toString())
        }

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

    override fun context(): Context {
        return this
    }

    override fun nickNameAdd(nickName: String) {
        name_edit.setText(nickName)
    }


}

fun abcd() {

}
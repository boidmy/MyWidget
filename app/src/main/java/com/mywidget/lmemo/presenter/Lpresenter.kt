package com.mywidget.lmemo.presenter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mywidget.SendPush
import com.mywidget.data.apiConnect.ApiConnection
import com.mywidget.lmemo.LmemoContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Lpresenter(view: LmemoContract.View) : LmemoContract.presenter {

    var mModel: LmemoContract.model? = null
    private var mView = WeakReference(view)
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Lmemo")
    private var userDb: DatabaseReference = FirebaseDatabase.getInstance().reference.child("User")
    private val disposable: CompositeDisposable = CompositeDisposable()

    fun setModel(model:LmemoContract.model) {
        mModel = model
    }

    fun getView(): LmemoContract.View? {
        if(mView != null) {
            return mView.get()
        } else {
            throw NullPointerException("View is unavailable")
        }
    }

    override fun setView(view: LmemoContract.View) {
        mView = WeakReference(view)
    }

    override fun clickMemoadd(name:String, memo:String) {
        memo(name, memo)
    }

    private fun memo(name:String, memo:String) {

        //val current = LocalDateTime.now()

        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        //val formatted = current.format(formatter)

        var result: HashMap<String, String> = hashMapOf()

        result["memo"] = memo
        result["date"] = "2020-11-11"

        database.child(name).push().setValue(result)

        findFriend(memo)

    }

    fun findFriend(memo: String) {
        val user = GoogleSignIn.getLastSignedInAccount(getView()?.context())?.email
        val idx = user?.indexOf("@")
        val userMail = idx?.let { user.substring(0, it) }?: ""

        val apiConnection = ApiConnection.Instance()
        disposable.add(apiConnection
            .selectNickName(userMail)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { item ->
                    findFriendToken(item.friendName?.split("@")?.first(), memo, item.nickName)
                }, {exception ->
                    Log.d("error!", exception.toString())
                }
            ))
    }

    fun findFriendToken(id: String?, memo: String, myNickname: String?) {
        val sendPush = SendPush()

        disposable.add(
            ApiConnection.Instance().retrofitService
            .userData(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                    //item -> sendPush.haha2(item, memo, myNickname)
            }, {
                Log.d("error", it.toString())
            })
        )
    }

    override fun selectNickName() {
        val user = GoogleSignIn.getLastSignedInAccount(getView()?.context())?.email
        val idx = user?.indexOf("@")
        val userMail: String = idx?.let { user.substring(0, it) }?:""

        disposable.add(
            ApiConnection.Instance()
            .selectNickName(userMail)
            .map { it.nickName }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { it ->
                    it?.let { getView()?.nickNameAdd(it) }
                }, {
                    Log.d("error : ", it.toString())
                })
        )
    }
}
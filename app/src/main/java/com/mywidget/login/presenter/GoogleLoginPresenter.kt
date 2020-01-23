package com.mywidget.login.presenter

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.mywidget.R
import com.mywidget.login.GoogleLoginContract
import kotlinx.android.synthetic.main.friend_add_dialog.view.*

class GoogleLoginPresenter(view: GoogleLoginContract.View) : GoogleLoginContract.presenter{

    private val mView = view

    private val RC_SIGN_IN = 101
    var mGoogleSignInClient: GoogleSignInClient?= null
    var mModel: GoogleLoginContract.model?= null

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("User")

    fun setModel(model: GoogleLoginContract.model) {
        mModel = model
    }

    override fun login(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val id = account?.id?:""
            val email = account?.email?:""
            var token: String
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { itemcontainer ->
                token = itemcontainer.token
                Log.d("aa", account?.email + "아디는" + account?.id)
                //loginDb(email, id, token)
                friendAdd(id, email, token)
            }
            // Signed in successfully, show authenticated UI.


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    private fun friendAdd(id: String, email: String, token: String) {
        var dialog = LayoutInflater.from(mView.context()).inflate(R.layout.friend_add_dialog, null)

        val popupWindow = PopupWindow(
            dialog,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isFocusable = true
        popupWindow.showAtLocation(dialog, Gravity.CENTER, 0, 0)
        dialog.name_txt.requestFocus()

        dialog.confirm_btn.setOnClickListener {
            singupDb(email, id, token, dialog.name_txt.text.toString(), dialog.nickname_txt.text.toString())
        }
    }

    private fun singupDb(email: String, id: String, token: String, friend: String, nickname: String) {

        var mEmail = email.substring(0, email.indexOf('@'))

        var result: HashMap<String, String> = hashMapOf()
        result["id"] = id
        result["email"] = email
        result["token"] = token
        result["friendName"] = friend
        result["nickName"] = nickname
        database.child(mEmail).setValue(result)
        mView.finishView()
    }

    override fun logout() {

    }
}
package com.mywidget.login.presenter

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mywidget.login.GoogleLoginContract

class GoogleLoginPresenter(view: GoogleLoginContract.View) : GoogleLoginContract.presenter{

    private val mView = view

    private val RC_SIGN_IN = 101
    var mGoogleSignInClient: GoogleSignInClient?= null
    var mModel: GoogleLoginContract.model?= null

    fun setModel(model: GoogleLoginContract.model) {
        mModel = model
    }

    override fun login(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            var id = account?.id
            var email = account?.email
            val token = account?.idToken
            // Signed in successfully, show authenticated UI.

            Log.d("aa", account?.email + "아디는" + account?.id)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    override fun logout() {

    }
}
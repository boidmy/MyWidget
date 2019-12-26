package com.mywidget.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mywidget.R
import com.mywidget.login.GoogleLoginContract
import com.mywidget.login.model.GoogleLoginModel
import com.mywidget.login.presenter.GoogleLoginPresenter
import kotlinx.android.synthetic.main.login_google.*


class LoginGoogle : AppCompatActivity(), GoogleLoginContract.View {

    var mGoogleSignInClient: GoogleSignInClient?= null
    var account: GoogleSignInAccount?= null

    private val RC_SIGN_IN = 101

    lateinit var mPresenter: GoogleLoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_google)

        sign_in_button.setOnClickListener(onClick)
        logout_button.setOnClickListener(onClick)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        account = GoogleSignIn.getLastSignedInAccount(this)

        setupMVP()
    }

    private fun setupMVP() {
        mPresenter = GoogleLoginPresenter(this)
        val model = GoogleLoginModel(mPresenter)
        mPresenter.setModel(model)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            mPresenter.login(task)
        }
    }

    private val onClick = View.OnClickListener { v ->
        when(v.id) {
            R.id.sign_in_button -> {
                signIn()
            }
            R.id.logout_button -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()
            ?.addOnCompleteListener(this) {
                // ...
            }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun googleSignInClient(): GoogleSignInClient? {
        return mGoogleSignInClient
    }
}
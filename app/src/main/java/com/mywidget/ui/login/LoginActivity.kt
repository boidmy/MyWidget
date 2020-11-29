package com.mywidget.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mywidget.R
import com.mywidget.databinding.ActivityLoginBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    var account: GoogleSignInAccount?= null
    private val RC_SIGN_IN = 101

    override val layout: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = GoogleSignIn.getLastSignedInAccount(this)
        signInGoogle.setOnClickListener { signInGoogleLogin() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        }
    }

    fun signUpBtn(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun signInGoogleLogin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            ?.addOnCompleteListener(this) {
                // ...
            }
    }
}
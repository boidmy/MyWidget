package com.mywidget.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityLoginBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.signup.SignUpActivity
import util.Util
import javax.inject.Inject


class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 101

    override val layout: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindView()
    }

    private fun bindView() {
        binding.activity = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                signInGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Log.w("Google", "Google sign in failed", e)
            }
        }
    }

    fun signUpBtn(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    fun signInPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(completeListener)
    }

    private fun signInGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(completeListener)
    }

    private var completeListener = OnCompleteListener { task: Task<AuthResult?> ->
        if (task.isSuccessful) {
            finish()
        } else {
            Util.toast(this, task.exception?.message?:"잠시 후 다시 시도해 주세요")
        }
    }
}
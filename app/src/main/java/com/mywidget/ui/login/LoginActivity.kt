package com.mywidget.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.mywidget.R
import com.mywidget.databinding.ActivityLoginBinding
import com.mywidget.ui.base.BaseActivity
import com.mywidget.ui.login.signup.SignUpActivity
import util.Util.toast
import javax.inject.Inject


class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> { factory }
    private val errorMsg = "잠시 후 다시 시도해 주세요"

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
            .addOnCompleteListener(passwordCompleteListener)
    }

    private fun signInGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(googleCompleteListener)
    }

    private var passwordCompleteListener = OnCompleteListener { task: Task<AuthResult?> ->
        successLogin(task)
    }

    private var googleCompleteListener = OnCompleteListener { task: Task<AuthResult?> ->
        val firstLoginChk = task.result?.additionalUserInfo?.isNewUser?: false
        if (firstLoginChk) {
            if (task.isSuccessful) {
                getUser()?.let { userVal ->
                    viewModel.singUpFirebase(userVal.email?:"", userVal.uid)
                    finish()
                }
            }
        } else {
            successLogin(task)
        }
    }

    private fun successLogin(task: Task<AuthResult?>) {
        if (task.isSuccessful) {
            viewModel.loginSetToken(getUser()?.email?:"")
            finish()
        } else {
            this.toast(task.exception?.message?: errorMsg)
        }
    }

    private fun getUser() : FirebaseUser? {
        return firebaseAuth.currentUser
    }
}
package com.mywidget.ui.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.mywidget.R
import com.mywidget.data.Constants.Companion.REQUEST_PASSWORD_SIGN_IN
import com.mywidget.data.Constants.Companion.REQUEST_RC_SIGN_IN
import com.mywidget.data.Landing
import com.mywidget.data.RESULT
import com.mywidget.data.RouterEvent
import com.mywidget.databinding.ActivityLoginBinding
import com.mywidget.databinding.ForgotPasswordDialogBinding
import com.mywidget.ui.base.BaseActivity
import util.LandingRouter.move
import util.Util.firebaseAuthException
import util.Util.toast
import javax.inject.Inject


class LoginActivity: BaseActivity<ActivityLoginBinding>() {

    @Inject lateinit var mGoogleSignInClient: GoogleSignInClient
    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var firebaseAuth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> { factory }
    private val forgotPasswordDialogBinding by lazy {
        ForgotPasswordDialogBinding.inflate(LayoutInflater.from(this)) }
    private val forgotPasswordDialog by lazy { Dialog(this, R.style.CustomDialogTheme) }

    override val layout: Int
        get() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        forgotPasswordDialog()
    }

    private fun bind() {
        binding.apply {
            activity = this@LoginActivity
            vm = viewModel
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                signInGoogle(account?.idToken!!)
            } catch (e: ApiException) {
                Log.w("Google", "Google sign in failed", e)
            }
        } else if (requestCode == REQUEST_PASSWORD_SIGN_IN) {
            val result: Boolean = data?.extras?.getBoolean(RESULT)?: false
            if (result) finish()
        }
    }

    fun signUpBtn(view: View) {
        move(RouterEvent(Landing.SIGN_UP))
    }

    fun signInPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(passwordCompleteListener)
    }

    private fun signInGoogle(idToken: String) {
        GoogleAuthProvider.getCredential(idToken, null).run {
            firebaseAuth.signInWithCredential(this)
                .addOnCompleteListener(googleCompleteListener)
        }
    }

    private var passwordCompleteListener = OnCompleteListener { task: Task<AuthResult?> ->
        successLogin(task)
    }

    private var googleCompleteListener = OnCompleteListener { task: Task<AuthResult?> ->
        val firstLoginChk = task.result?.additionalUserInfo?.isNewUser?: false
        if (firstLoginChk) {
            if (task.isSuccessful) {
                getUser()?.let { userVal ->
                    viewModel.singUpFirebase(userVal.email?:""
                        , userVal.uid, userVal.displayName?:"")
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
            toast(task.exception?.message?: getString(R.string.error))
        }
    }

    private fun getUser(): FirebaseUser? = firebaseAuth.currentUser

    fun forgotPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                toast(getString(R.string.passwordReset))
                viewModel.forgotPasswordDialogVisibility(false)
            } else {
                this firebaseAuthException (task.exception as FirebaseAuthException).errorCode
            }
        }
    }

    private fun forgotPasswordDialog() {
        forgotPasswordDialog.setContentView(forgotPasswordDialogBinding.root)
        forgotPasswordDialogBinding.activity = this
        viewModel.forgotPasswordDialogVisibility.observe(this, Observer {
            if (it) forgotPasswordDialog.show()
            else forgotPasswordDialog.dismiss()
        })
    }

    private fun progress() {
        //추후 테스트후 사용 여부 결정
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_progress)
        binding.progressImg.animation = animation
    }

}
package com.mywidget.login

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task

interface GoogleLoginContract {

    interface View {
        fun googleSignInClient(): GoogleSignInClient?
    }

    interface presenter {
        fun login(completedTask: Task<GoogleSignInAccount>)
        fun logout()
    }

    interface model {

    }
}
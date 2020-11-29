package com.mywidget.ui.login

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.mywidget.R
import dagger.Module
import dagger.Provides

@Module
class LoginGoogleModule {

    @Provides
    fun provideGso(activity: LoginActivity): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    @Provides
    fun provideGoogleSignInClient(
        activity: LoginActivity,
        gso: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(activity, gso)
    }

}
package com.mywidget.di.module

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class GoogleLoginModule {

    @Provides
    fun bindGetLastSigned(application: Application): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(application)
    }
}
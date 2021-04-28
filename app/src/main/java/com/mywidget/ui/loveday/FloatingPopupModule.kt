package com.mywidget.ui.loveday

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mywidget.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FloatingPopupModule {

    @Provides
    @Named("rotateOpen")
    fun rotateOpenAnimation(activity: FloatingPopupActivity): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.rotate_floating_open)
    }

    @Provides
    @Named("rotateClose")
    fun rotateCloseAnimation(activity: FloatingPopupActivity): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.rotate_floating_close)
    }

    @Provides
    @Named("floatingOpen")
    fun floatingOpenAnimation(activity: FloatingPopupActivity): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_floating_open)
    }

    @Provides
    @Named("floatingClose")
    fun floatingCloseAnimation(activity: FloatingPopupActivity): Animation {
        return AnimationUtils.loadAnimation(activity, R.anim.anim_floating_close)
    }
}
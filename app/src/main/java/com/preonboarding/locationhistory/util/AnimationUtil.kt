package com.preonboarding.locationhistory.util

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.preonboarding.locationhistory.R

object AnimationUtil {
    fun shakeAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
}
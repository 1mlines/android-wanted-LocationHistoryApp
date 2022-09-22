package com.preonboarding.locationhistory.util

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.preonboarding.locationhistory.R

object AnimationUtil {
    //내용 입력 안 하면 흔들흔들~
    fun shakeAnimation(context: Context): Animation = AnimationUtils.loadAnimation(context, R.anim.shake)
}
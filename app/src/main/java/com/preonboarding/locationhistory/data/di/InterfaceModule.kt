package com.preonboarding.locationhistory.data.di

import android.app.Activity
import com.preonboarding.locationhistory.presentation.ui.main.LoadUrlCallbackInterface
import com.preonboarding.locationhistory.presentation.ui.main.LocationCallbackInterface
import com.preonboarding.locationhistory.presentation.ui.main.ShowMessageCallbackInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object InterfaceModule {

    @Provides
    @ActivityScoped
     fun bindLoadUrlCallback(
        activity: Activity
    ): LoadUrlCallbackInterface? = activity as? LoadUrlCallbackInterface


    @Provides
    @ActivityScoped
     fun bindLocationCallback(
        activity: Activity
    ): LocationCallbackInterface? = activity as? LocationCallbackInterface

    @Provides
    @ActivityScoped
     fun bindShowMsgCallback(
        activity: Activity
    ): ShowMessageCallbackInterface? = activity as? ShowMessageCallbackInterface

}
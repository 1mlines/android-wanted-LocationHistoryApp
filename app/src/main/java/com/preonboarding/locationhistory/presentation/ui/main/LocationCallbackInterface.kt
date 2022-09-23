package com.preonboarding.locationhistory.presentation.ui.main

import com.preonboarding.locationhistory.domain.model.Location

@FunctionalInterface
interface LocationCallbackInterface {

    fun getCurrentLocation(location: Location)
}
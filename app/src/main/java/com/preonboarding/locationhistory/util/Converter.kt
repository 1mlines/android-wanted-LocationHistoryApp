package com.preonboarding.locationhistory.util

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun radians(degree: Double): Double {
    return (180 * degree) / PI
}

fun distanceConverter(latitude: Double, longitude: Double): Double {
    return 6371 * acos(
        cos(radians(37.4685225)) * cos(radians(latitude))
                * cos(radians(longitude) - radians(126.8943311))
                + sin(radians(37.4685225)) * sin(radians(latitude))
    )
}
package com.preonboarding.locationhistory.util

import android.content.pm.PackageManager

object PermissionUtils {

    fun isPermissionGranted(
        permissions: Array<out String>,
        grantResults: IntArray,
        permission: String
    ): Boolean {
        for (idx in permissions.indices) {
            if (permission == permissions[idx]) {
                return grantResults[idx] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }
}
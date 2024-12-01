package com.uwange.permissionchecker

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED

internal object PermissionCheckerUtil {

    fun Context.checkPermissionGranted(permissions: List<String>): Boolean {
        return permissions.all {
            checkSelfPermission(it) == PERMISSION_GRANTED
        }
    }
}
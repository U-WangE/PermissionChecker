package com.uwange.permissionchecker

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED

internal object PermissionCheckerUtil {
    fun getDeniedPermissions(permissions: List<String>): List<String> {
        return permissions.filter { !it.value }.map { it.key }
    }

    fun getGrantedPermissions(permissions: List<String>): List<String> {
        return permissions.filter { it.value }.map { it.key }
    }

    fun Context.checkPermissionGranted(permissions: List<String>): Boolean {
        return permissions.all {
            checkSelfPermission(it) == PERMISSION_GRANTED
        }
    }
}
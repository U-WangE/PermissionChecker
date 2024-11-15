package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION

internal class Location(
    private val type: Type
): PermissionCheckAndRequest(type) {

    override fun getPermissions(): Array<String> {
        return when (type) {
            Type.Location -> arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            else -> emptyArray()
        }
    }

    override fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean? {
        return when (type) {
            Type.Location -> {
                permissions[ACCESS_FINE_LOCATION] == true &&
                        permissions[ACCESS_COARSE_LOCATION] == true
            }
            else -> null
        }
    }
}
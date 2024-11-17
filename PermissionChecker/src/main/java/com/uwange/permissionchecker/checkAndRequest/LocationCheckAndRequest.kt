package com.uwange.permissionchecker.checkAndRequest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import com.uwange.permissionchecker.LocationType
import com.uwange.permissionchecker.Type

internal class LocationCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {

    override fun getPermissions(): Array<String> {
        return when (type) {
            LocationType.Location -> arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            else -> emptyArray()
        }
    }

    override fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean? {
        return when (type) {
            LocationType.Location -> {
                permissions[ACCESS_FINE_LOCATION] == true &&
                        permissions[ACCESS_COARSE_LOCATION] == true
            }
            else -> null
        }
    }
}
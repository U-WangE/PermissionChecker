package com.uwange.permissionchecker.checkAndRequest

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class LocationCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {

    override fun getPermissions(): Array<String> {
        return when (type) {
            Type.LocationType.Location -> arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            Type.LocationType.LocationAlways -> arrayOf(ACCESS_BACKGROUND_LOCATION)
            else -> emptyArray()
        }
    }

    override fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean? {
        return when (type) {
            Type.LocationType.Location -> {
                permissions[ACCESS_FINE_LOCATION] == true &&
                        permissions[ACCESS_COARSE_LOCATION] == true
            }
            Type.LocationType.LocationAlways -> {
                permissions[ACCESS_BACKGROUND_LOCATION] == true
            }
            else -> null
        }
    }

    override fun handlePermissionLauncher(
        permissions: Array<String>,
        launcher: ActivityResultLauncher<Array<String>>
    ) {
        when (type) {
            Type.LocationType.Location -> {
                super.handlePermissionLauncher(permissions, launcher)
            }
            Type.LocationType.LocationAlways -> {
                val locationPermissions = listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

                val isPermissionGranted = locationPermissions.all {
                    activity.checkSelfPermission(it) == PERMISSION_GRANTED
                }
                if (isPermissionGranted)
                    super.handlePermissionLauncher(permissions, launcher)
                else
                    resultLiveData?.postValue(PermissionResponse(false, "$type First you need to declare location permissions", type, listOf(), locationPermissions))
            }
            else -> {
                resultLiveData?.postValue(PermissionResponse(false, "$type Permission Type Miss Match", type))
            }
        }
    }
}
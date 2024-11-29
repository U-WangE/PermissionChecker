package com.uwange.permissionchecker.checkAndRequest

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class LocationCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {

    override fun getPermissions(): List<String> {
        return when (type) {
            Type.LocationType.Location -> listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            Type.LocationType.LocationAlways -> listOf(ACCESS_BACKGROUND_LOCATION)
            else -> emptyList()
        }
    }

    override fun launchLauncher(
        permissions: List<String>,
        launcher: ActivityResultLauncher<Array<String>>
    ) {
        when (type) {
            Type.LocationType.Location -> {
                super.launchLauncher(permissions, launcher)
            }
            Type.LocationType.LocationAlways -> {
                val locationPermissions = listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

                val isPermissionGranted = locationPermissions.all {
                    activity.checkSelfPermission(it) == PERMISSION_GRANTED
                }
                if (isPermissionGranted)
                    super.launchLauncher(permissions, launcher)
                else
                    resultLiveData?.postValue(PermissionResponse(false, "$type First you need to declare location permissions", type, listOf(), locationPermissions))
            }
            else -> {
                resultLiveData?.postValue(PermissionResponse(false, "$type Permission Type Miss Match", type))
            }
        }
    }
}
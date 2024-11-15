package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher

internal class Location(
private val type: Type
) {

    fun request(
        activity: Activity,
        launcher: ActivityResultLauncher<Array<String>>?,
        callBack: (Response) -> Unit
    ) {
        when (type) {
            Type.Location -> {
                val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

                val isPermissionGranted = permissions.all {
                    activity.checkSelfPermission(it) == PERMISSION_GRANTED
                }
                if (!isPermissionGranted)
                    launcher?.launch((permissions))
                else
                    callBack(Response(true, "$type Permission Already Granted", type))
            }
            else -> {
            }
        }
    }

    fun checkGrant(permissions: Map<String, Boolean>): Response {
        return when (type) {
            Type.Location -> {
                val isGranted =
                    (permissions[ACCESS_FINE_LOCATION] == true &&
                            permissions[ACCESS_COARSE_LOCATION] == true)
                Response(isGranted, "$type Permission ${if (isGranted) "Granted" else "Denied"}", type)
            }
            else -> {
                Response(true, "")
            }
        }
    }
}
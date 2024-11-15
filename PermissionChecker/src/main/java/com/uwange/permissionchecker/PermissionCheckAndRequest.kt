package com.uwange.permissionchecker

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher

abstract class PermissionCheckAndRequest(
    private val type: Type
) {
    internal abstract fun getPermissions(): Array<String>
    internal abstract fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean?

    open fun request(
        activity: Activity,
        launcher: ActivityResultLauncher<Array<String>>,
        callBack: (Response) -> Unit
    ) {
        val permissions = getPermissions()
        if (permissions.isEmpty()) {
            callBack(Response(false, "$type Permission Type Miss Match", type))
            return
        }

        val isPermissionGranted = permissions.all {
            activity.checkSelfPermission(it) == PERMISSION_GRANTED
        }

        if (isPermissionGranted)
            callBack(Response(true, "$type Permission Already Granted", type))
        else
            launcher.launch((permissions))
    }

    open fun checkGrant(permissions: Map<String, Boolean>): Response {
        val isGranted = isPermissionsGranted(permissions)

        return if (isGranted == null)
            Response(false, "$type Permission Type Miss Match", type)
        else
            Response(isGranted, "$type Permission ${if (isGranted) "Granted" else "Denied"}", type)
    }
}
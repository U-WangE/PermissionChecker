package com.uwange.permissionchecker.checkAndRequest

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type

abstract class PermissionCheckAndRequest(
    private val type: Type
) {
    internal abstract fun getPermissions(): Array<String>
    internal abstract fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean?

    open fun request(
        activity: Activity,
        launcher: ActivityResultLauncher<Array<String>>,
        callBack: (PermissionResponse) -> Unit
    ) {
        val permissions = getPermissions()
        if (permissions.isEmpty()) {
            callBack(PermissionResponse(false, "$type Permission Type Miss Match", type))
            return
        }

        val isPermissionGranted = permissions.all {
            activity.checkSelfPermission(it) == PERMISSION_GRANTED
        }

        if (isPermissionGranted)
            callBack(PermissionResponse(true, "$type Permission Already Granted", type, permissions.toList()))
        else
            launcher.launch((permissions))
    }

    open fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        val isGranted = isPermissionsGranted(permissions)
        val grantedPermissions = getGrantedPermissions(permissions)
        val deniedPermissions = getDeniedPermissions(permissions)

        return if (isGranted == null)
            PermissionResponse(false, "$type Permission Type Miss Match", type, grantedPermissions, deniedPermissions)
        else if (isGranted)
            PermissionResponse(isGranted, "$type Permission Granted", type, grantedPermissions, deniedPermissions)
        else
            PermissionResponse(isGranted, "$type Permission Denied", type, grantedPermissions, deniedPermissions)
    }

    private fun getDeniedPermissions(permissions: Map<String, Boolean>): List<String> {
        return permissions.filter { !it.value }.map { it.key }
    }

    private fun getGrantedPermissions(permissions: Map<String, Boolean>): List<String> {
        return permissions.filter { it.value }.map { it.key }
    }
}
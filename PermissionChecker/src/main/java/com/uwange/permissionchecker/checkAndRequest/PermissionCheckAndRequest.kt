package com.uwange.permissionchecker.checkAndRequest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.uwange.permissionchecker.PermissionCheckerApp.Companion.permissionCheckerPreference
import com.uwange.permissionchecker.PermissionCheckerUtil.checkPermissionGranted
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type

internal abstract class PermissionCheckAndRequest(
    private val activity: Activity,
    private val type: Type
) {
    internal abstract fun getPermissions(): List<String>
    internal open fun isPermissionsGranted(permissions: List<String>): Boolean? = activity.checkPermissionGranted(permissions)
    internal open fun checkAllPermission(permissions: List<String>): Boolean {
        return permissions.all {
            activity.checkSelfPermission(it) == PERMISSION_GRANTED
        }
    }
    internal open fun checkDeniedMoreThanTwice(permissions: List<String>): Boolean {
        // isDeniedMoreThanTwice : "처음 요청" 또는 "2번 이상 거부시 true" 또는 "권한 전부 수락시", "처음 거부시" false,
        return !permissions.all {
            shouldShowRequestPermissionRationale(activity, it)
        }
    }
    internal open fun handleLauncher(permissions: List<String>, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>) {
        when {
            // 2회 이상 거부 됐고, 직전 권한 요청 방식이 Intent Launcher 가 아닌 경우 실행
            checkDeniedMoreThanTwice(permissions) && !permissionCheckerPreference.checkFirstTime && !permissionCheckerPreference.lastActionIsIntentLauncher -> {
                launchIntentLauncher(intentLauncher = intentLauncher)
            }
            else -> launchLauncher(permissions, launcher)
        }
    }
    internal open fun launchLauncher(permissions: List<String>, launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(permissions.toTypedArray())
    }
    internal open fun launchIntentLauncher(intentLauncher: ActivityResultLauncher<Intent>) {
        intentLauncher.launch(Intent().apply {
            action = ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity.packageName, null)
        })
    }

    internal fun request(
        launcher: ActivityResultLauncher<Array<String>>,
        intentLauncher: ActivityResultLauncher<Intent>,
        callBack: (PermissionResponse) -> Unit
    ) {
        val permissions = getPermissions()
        if (permissions.isEmpty()) {
            callBack(PermissionResponse(false, "$type Permission Type Miss Match", type))
            return
        }
        when {
            checkAllPermission(permissions) -> callBack(PermissionResponse(true, "$type Permission Already Granted", type, permissions.toList()))
            else -> handleLauncher(permissions, launcher, intentLauncher)
        }
    }

    internal fun checkGrant(permissions: List<String>): PermissionResponse {
        val isGrantedAll = isPermissionsGranted(permissions)
        val grantedPermissions = getGrantedPermissions(permissions)
        val deniedPermissions = getDeniedPermissions(permissions)

        val isDeniedMoreThanTwice = !deniedPermissions.all {
            shouldShowRequestPermissionRationale(activity, it)
        }

        return if (isGrantedAll == null)
            PermissionResponse(false, "$type Permission Type Miss Match", type, grantedPermissions, deniedPermissions)
        else if (isGrantedAll)
            PermissionResponse(isGrantedAll, "$type Permission Granted", type, grantedPermissions, deniedPermissions)
        else
            PermissionResponse(isGrantedAll, "$type Permission Denied", type, grantedPermissions, deniedPermissions, isDeniedMoreThanTwice)
        return PermissionResponse(false, "",type)
    }
}
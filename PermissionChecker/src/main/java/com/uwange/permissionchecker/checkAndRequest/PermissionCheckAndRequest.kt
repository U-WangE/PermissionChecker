package com.uwange.permissionchecker.checkAndRequest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.uwange.permissionchecker.PermissionCheckerApp.Companion.permissionCheckerPreference
import com.uwange.permissionchecker.PermissionCheckerUtil.getDeniedPermissions
import com.uwange.permissionchecker.PermissionCheckerUtil.getGrantedPermissions
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type

internal abstract class PermissionCheckAndRequest(
    private val activity: Activity,
    private val type: Type
) {
    internal abstract fun getPermissions(): Array<String>
    internal abstract fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean?
// 권한 전체가 처음 요청 중인지 판단하는 perference만 있으면 될듯
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

        val isPermissionGranted = permissions.all {
            activity.checkSelfPermission(it) == PERMISSION_GRANTED
        }

        // isDeniedMoreThanTwice : "처음 요청" 또는 "2번 이상 거부시 true" 또는 "권한 전부 수락시", "처음 거부시" false,
        val isDeniedMoreThanTwice = !permissions.all {
            shouldShowRequestPermissionRationale(activity, it)
        }

        when {
            isPermissionGranted -> callBack(PermissionResponse(true, "$type Permission Already Granted", type, permissions.toList()))

            // 2회 이상 거부 됐고, 직전 권한 요청 방식이 Intent Launcher 가 아닌 경우 실행
            isDeniedMoreThanTwice && !permissionCheckerPreference.checkFirstTime && !permissionCheckerPreference.lastActionIsIntentLauncher -> {
                //TODO:: 각 권한에 따라 처리 로직 필요
                intentLauncher.launch(Intent().apply {
                    action = ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", activity.packageName, null)
                })
            }
            else -> launcher.launch((permissions))
        }
    }

    internal fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
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
    }
}
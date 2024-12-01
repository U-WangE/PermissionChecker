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
    internal open fun isPermissionsGranted(permissions: List<String>): Boolean = activity.checkPermissionGranted(permissions)
    internal open fun checkDeniedMoreThanTwice(permissions: List<String>): Boolean {
        // isDeniedMoreThanTwice : "처음 요청" 또는 "2번 이상 거부시 true" 또는 "권한 전부 수락시", "처음 거부시" false,
        return !permissions.all {
            shouldShowRequestPermissionRationale(activity, it)
        }
    }
    internal open fun checkPermissions(permissions: List<String>): Pair<List<String>, List<String>> {
        val granted: ArrayList<String> = arrayListOf()
        val denied: ArrayList<String> = arrayListOf()

        permissions.all {
            if (activity.checkSelfPermission(it) == PERMISSION_GRANTED)
                granted.add(it)
            else
                denied.add(it)
        }
        return Pair(granted.toList(), denied.toList())
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
            isPermissionsGranted(permissions) -> callBack(PermissionResponse(true, "$type Permission Already Granted", type, permissions.toList()))
            else -> handleLauncher(permissions, launcher, intentLauncher)
        }
    }

    internal fun checkGrant(permissions: List<String>): PermissionResponse {
        val isGrantedAll = isPermissionsGranted(permissions)
        val checkPermissions = checkPermissions(permissions)

        val isDeniedMoreThanTwice = !checkPermissions.second.all {
            shouldShowRequestPermissionRationale(activity, it)
        }

        return if (isGrantedAll)
            PermissionResponse(true, "$type Permission Granted", type, checkPermissions.first, checkPermissions.second)
        else
            PermissionResponse(false, "$type Permission Denied", type, checkPermissions.first, checkPermissions.second, isDeniedMoreThanTwice)
    }
}
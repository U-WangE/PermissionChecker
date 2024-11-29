package com.uwange.permissionchecker.checkAndRequest

import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.PermissionCheckerApp.Companion.permissionCheckerPreference
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class ETCCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {

    override fun getPermissions(): List<String> {
        return when (type) {
            Type.ETCType.Overlay -> {
                listOf(SYSTEM_ALERT_WINDOW)
            }
            else -> emptyList()
        }
    }

    override fun isPermissionsGranted(permissions: List<String>): Boolean? {
        return when (type) {
            Type.ETCType.Overlay -> {
                Settings.canDrawOverlays(activity)
            }
            else -> null
        }
    }

    override fun checkAllPermission(permissions: List<String>): Boolean {
        return when (type) {
            Type.ETCType.Overlay -> Settings.canDrawOverlays(activity)
            else -> super.checkAllPermission(permissions)
        }
    }

    // isDeniedMoreThanTwice : "처음 요청" 또는 "2번 이상 거부시 true" 또는 "권한 전부 수락시", "처음 거부시" false,
    override fun checkDeniedMoreThanTwice(permissions: List<String>): Boolean {
        return when (type) {
            Type.ETCType.Overlay -> {
                false
            }
            else -> false
        }
    }

    override fun handleLauncher(
        permissions: List<String>,
        launcher: ActivityResultLauncher<Array<String>>,
        intentLauncher: ActivityResultLauncher<Intent>
    ) {
        if (!permissionCheckerPreference.lastActionIsIntentLauncher) {
            when (type) {
                Type.ETCType.Overlay -> {
                    super.launchIntentLauncher(intentLauncher)
                    try {
                        intentLauncher.launch(Intent().apply {
                            action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                            data = Uri.fromParts("package", activity.packageName, null)
                        })
                    } catch (e: Exception) {
                        super.launchIntentLauncher(intentLauncher)
                    }
                }
                else -> {
                    resultLiveData?.postValue(PermissionResponse(false, "$type Permission Type Miss Match", type))
                }
            }
        } else {
            permissionCheckerPreference.lastActionIsIntentLauncher = false
            if (Settings.canDrawOverlays(activity))
                resultLiveData?.postValue(PermissionResponse(true, "$type Permission Granted", type, permissions.toList()))
            else
                resultLiveData?.postValue(PermissionResponse(false, "$type Permission Denied", type, emptyList(), permissions.toList()))
        }
    }
}
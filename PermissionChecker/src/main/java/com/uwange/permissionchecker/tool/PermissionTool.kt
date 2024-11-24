package com.uwange.permissionchecker.tool

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type


internal interface PermissionTool {
    fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse
    fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>)
}
package com.uwange.permissionchecker.tool

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.ETCCheckAndRequest
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class ETCPermission(
    private val activity: AppCompatActivity
): PermissionTool {
    private var etcCheckAndRequest: ETCCheckAndRequest? = null

    override fun checkGrant(permissions: List<String>): PermissionResponse {
        return etcCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    override fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>) {
        if (type is Type.ETCType) {
            type.let {
                etcCheckAndRequest = ETCCheckAndRequest(activity, it)
                etcCheckAndRequest?.request(launcher, intentLauncher) { permissionResponse ->
                    resultLiveData?.postValue(permissionResponse)
                }
            }?:resultLiveData?.postValue(PermissionResponse(false, "Permission Type is null.", type))
        }
    }
}
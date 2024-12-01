package com.uwange.permissionchecker.tool

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.BluetoothCheckAndRequest
import com.uwange.permissionchecker.checkAndRequest.ETCCheckAndRequest
import com.uwange.permissionchecker.checkAndRequest.LocationCheckAndRequest
import com.uwange.permissionchecker.checkAndRequest.PermissionCheckAndRequest
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData


internal class PermissionTools (
    private val activity: AppCompatActivity,
) {
    private var permissionCheckAndRequest: PermissionCheckAndRequest? = null
    
    fun checkGrant(permissions: List<String>): PermissionResponse {
        return permissionCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>) {
        permissionCheckAndRequest = when (type) {
            is Type.BluetoothType -> {
                BluetoothCheckAndRequest(activity, type)
            }
            is Type.LocationType -> {
                LocationCheckAndRequest(activity, type)
            }
            is Type.ETCType -> {
                ETCCheckAndRequest(activity, type)
            }
            else -> null
        }

        type.let {
            permissionCheckAndRequest?.request(launcher, intentLauncher) { permissionResponse ->
                resultLiveData?.postValue(permissionResponse)
            }
        }?: resultLiveData?.postValue(PermissionResponse(false, "Permission Type is null.", type))
    }
}
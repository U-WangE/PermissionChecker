package com.uwange.permissionchecker.tool

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.LocationCheckAndRequest
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class LocationPermission(
    private val activity: AppCompatActivity
): PermissionTool {
    private var locationCheckAndRequest: LocationCheckAndRequest? = null

    override fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        return locationCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    override fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>) {
        if (type in listOf(Type.LocationType.Location)) {
            type?.let {
                locationCheckAndRequest = LocationCheckAndRequest(activity, it)
                locationCheckAndRequest?.request(launcher, intentLauncher) { permissionResponse ->
                    resultLiveData?.postValue(permissionResponse)
                }
            }?:resultLiveData?.postValue(PermissionResponse(false, "Permission Type is null.", type))
        }
    }
}
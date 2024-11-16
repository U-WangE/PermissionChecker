package com.uwange.permissionchecker.manager

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.LocationType
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.LocationCheckAndRequest

class LocationPermission(private val activity: AppCompatActivity): PermissionChecker(activity) {
    private var locationCheckAndRequest: LocationCheckAndRequest? = null

    override fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        return locationCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    override fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>) {
        if (type in listOf(LocationType.Location)) {
            locationCheckAndRequest = LocationCheckAndRequest(type)
            locationCheckAndRequest?.request(activity, launcher) {
                resultLiveData?.postValue(it)
            }
        }
    }
}
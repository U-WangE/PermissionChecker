package com.uwange.permissionchecker.tool

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.BluetoothCheckAndRequest
import com.uwange.permissionchecker.manager.PermissionChecker.Companion.resultLiveData

internal class BluetoothPermission(
    private val activity: AppCompatActivity
): PermissionTool {
    private var bluetoothCheckAndRequest: BluetoothCheckAndRequest? = null

    override fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        return bluetoothCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    override fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>) {
        if (type in listOf(
                Type.BluetoothType.Bluetooth,
                Type.BluetoothType.BluetoothScan,
                Type.BluetoothType.BluetoothConnect,
                Type.BluetoothType.BluetoothAdvertise,
                Type.BluetoothType.BluetoothALL
            )) {
            type?.let {
                bluetoothCheckAndRequest = BluetoothCheckAndRequest(activity, it)
                bluetoothCheckAndRequest?.request(launcher, intentLauncher) { permissionResponse ->
                    resultLiveData?.postValue(permissionResponse)
                }
            }?:resultLiveData?.postValue(PermissionResponse(false, "Permission Type is null.", type))
        }
    }
}
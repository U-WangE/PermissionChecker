package com.uwange.permissionchecker.manager

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.BluetoothType
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.checkAndRequest.BluetoothCheckAndRequest

class BluetoothPermission(private val activity: AppCompatActivity): PermissionChecker(activity) {
    private var bluetoothCheckAndRequest: BluetoothCheckAndRequest? = null

    override fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        return bluetoothCheckAndRequest?.checkGrant(permissions) ?: PermissionResponse(false, "Permission Checker not initialized.")
    }

    override fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>) {
        if (type in listOf(
                BluetoothType.Bluetooth,
                BluetoothType.BluetoothScan,
                BluetoothType.BluetoothConnect,
                BluetoothType.BluetoothAdvertise,
                BluetoothType.BluetoothALL
            )) {
            bluetoothCheckAndRequest = BluetoothCheckAndRequest(type)
            bluetoothCheckAndRequest?.request(activity, launcher) {
                resultLiveData?.postValue(it)
            }
        }
    }
}
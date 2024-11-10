package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import com.uwange.permissionchecker.Type.Bluetooth
import com.uwange.permissionchecker.Type.BluetoothAdmin
import com.uwange.permissionchecker.Type.BluetoothAdvertise
import com.uwange.permissionchecker.Type.BluetoothConnect
import com.uwange.permissionchecker.Type.BluetoothScan

internal class Bluetooth(
    activity: Activity,
    private val type: Type,
    val launcher: ActivityResultLauncher<Array<String>>?,
    private val callBack: (Response) -> Unit
) {
    init {
        requestBluetoothPermission(activity)
    }

    private fun requestBluetoothPermission(activity: Activity) {
        when (type) {
            Bluetooth -> {
                val permissions =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        arrayOf(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_SCAN)
                    else
                        arrayOf(ACCESS_FINE_LOCATION, BLUETOOTH)

                val permissionGranted = permissions.all {
                    activity.checkSelfPermission(it) == PERMISSION_GRANTED
                }
                if (!permissionGranted)
                    launcher?.launch((permissions))
                else
                    callBack(Response(true, "$type Permission Granted"))
            }
            BluetoothScan -> {
                true
            }
            BluetoothAdmin -> {
                true
            }
            BluetoothConnect -> {
                true
            }
            BluetoothAdvertise -> {
                true
            }
            else -> { true }
        }
    }


//    private fun permissionChecker(
//        vararg permissions: String,
//        onPermissionResult: (Boolean) -> Unit
//    ) {
//        val permissionGranted = permissions.all {
//            checkSelfPermission(it) == PERMISSION_GRANTED
//        }
//
//        // onPermissionResult : Boolean 해당 변수로 권한 수락 유무를 판단
//        if (permissionGranted) {
//            onPermissionResult(true)
//        } else {
//            onPermissionResult(false)
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
//        }
//    }

}
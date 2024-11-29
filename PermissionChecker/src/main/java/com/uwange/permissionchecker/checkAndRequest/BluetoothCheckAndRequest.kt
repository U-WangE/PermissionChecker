package com.uwange.permissionchecker.checkAndRequest

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.uwange.permissionchecker.Type

internal class BluetoothCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {
    override fun getPermissions(): Array<String> {
        return when (type) {
            Type.BluetoothType.Bluetooth -> arrayOf(BLUETOOTH)
            Type.BluetoothType.BluetoothScan -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH_SCAN)
                else
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(BLUETOOTH_CONNECT)
                else
                    arrayOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothAdvertise -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(BLUETOOTH_ADVERTISE)
                else
                    arrayOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothALL -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH_SCAN, BLUETOOTH_CONNECT, BLUETOOTH_ADVERTISE)
                else
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
            }
            else -> emptyArray()
        }
    }

    override fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean? {
        return when (type) {
            Type.BluetoothType.Bluetooth -> permissions[BLUETOOTH] == true
            Type.BluetoothType.BluetoothScan -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_SCAN] == true &&
                            permissions[ACCESS_FINE_LOCATION] == true &&
                            permissions[ACCESS_COARSE_LOCATION] == true
                else
                    permissions[BLUETOOTH] == true &&
                            permissions[BLUETOOTH_ADMIN] == true &&
                            permissions[ACCESS_FINE_LOCATION] == true &&
                            permissions[ACCESS_COARSE_LOCATION] == true
            }
            Type.BluetoothType.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_CONNECT] == true
                else
                    permissions[BLUETOOTH] == true && permissions[BLUETOOTH_ADMIN] == true
            }
            Type.BluetoothType.BluetoothAdvertise -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_ADVERTISE] == true
                else
                    permissions[BLUETOOTH] == true && permissions[BLUETOOTH_ADMIN] == true
            }
            Type.BluetoothType.BluetoothALL -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_SCAN] == true &&
                            permissions[BLUETOOTH_CONNECT] == true &&
                            permissions[BLUETOOTH_ADVERTISE] == true &&
                            permissions[ACCESS_FINE_LOCATION] == true &&
                            permissions[ACCESS_COARSE_LOCATION] == true
                else
                    permissions[BLUETOOTH] == true &&
                            permissions[BLUETOOTH_ADMIN] == true &&
                            permissions[ACCESS_FINE_LOCATION] == true &&
                            permissions[ACCESS_COARSE_LOCATION] == true
            }
            else -> null
        }
    }

    override fun launchIntentLauncher(intentLauncher: ActivityResultLauncher<Intent>) {
        try {
            intentLauncher.launch(Intent().apply {
                action = ACTION_BLUETOOTH_SETTINGS
                data = Uri.fromParts("package", activity.packageName, null)
            })
        } catch (e: Exception) {
            super.launchIntentLauncher(intentLauncher)
        }
    }
}
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
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.uwange.permissionchecker.PermissionCheckerUtil.checkPermissionGranted
import com.uwange.permissionchecker.Type

internal class BluetoothCheckAndRequest(
    private val activity: Activity,
    private val type: Type
): PermissionCheckAndRequest(activity, type) {
    override fun getPermissions(): List<String> {
        return when (type) {
            Type.BluetoothType.Bluetooth -> listOf(BLUETOOTH)
            Type.BluetoothType.BluetoothScan -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH_SCAN)
                else
                    listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    listOf(BLUETOOTH_CONNECT)
                else
                    listOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothAdvertise -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    listOf(BLUETOOTH_ADVERTISE)
                else
                    listOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothType.BluetoothALL -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH_SCAN, BLUETOOTH_CONNECT, BLUETOOTH_ADVERTISE)
                else
                    listOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
            }
            else -> emptyList()
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
package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.os.Build

internal class Bluetooth(
    private val type: Type
): PermissionCheckAndRequest(type) {
    override fun getPermissions(): Array<String> {
        return when (type) {
            Type.Bluetooth -> arrayOf(BLUETOOTH)
            Type.BluetoothScan -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH_SCAN)
                else
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(BLUETOOTH_CONNECT)
                else
                    arrayOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothAdvertise -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    arrayOf(BLUETOOTH_ADVERTISE)
                else
                    arrayOf(BLUETOOTH, BLUETOOTH_ADMIN)
            }
            Type.BluetoothALL -> {
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
            Type.Bluetooth -> permissions[BLUETOOTH] == true
            Type.BluetoothScan -> {
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
            Type.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_CONNECT] == true
                else
                    permissions[BLUETOOTH] == true && permissions[BLUETOOTH_ADMIN] == true
            }
            Type.BluetoothAdvertise -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    permissions[BLUETOOTH_ADVERTISE] == true
                else
                    permissions[BLUETOOTH] == true && permissions[BLUETOOTH_ADMIN] == true
            }
            else -> null
        }
    }
}
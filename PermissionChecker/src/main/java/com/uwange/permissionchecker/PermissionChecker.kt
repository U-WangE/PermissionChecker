package com.uwange.permissionchecker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.uwange.permissionchecker.Type.*

enum class Type {
    Location,
    Bluetooth, BluetoothScan, BluetoothAdmin, BluetoothConnect, BluetoothAdvertise

}

class PermissionChecker(
    private val activity: Activity
) {
    companion object {
        val a = ""

    }

    private var launcher: ActivityResultLauncher<Array<String>>? = null


    fun result(callback: () -> Unit) {
        launcher = (activity as AppCompatActivity).registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (permissions[BLUETOOTH] == true &&
                    permissions[BLUETOOTH_SCAN] == true &&
                    permissions[ACCESS_FINE_LOCATION] == true) {
                    Log.d("여기1", permissions.toString())
                    Log.d("여기1", permissions.keys.toString())
                }
            } else {
                if (permissions[BLUETOOTH] == true &&
                    permissions[ACCESS_FINE_LOCATION] == true) {
                    Log.d("여기1", permissions.toString())
                    Log.d("여기1", permissions.keys.toString())
                }
            }
        }
    }

    fun request(type: Type) {
        when (type) {
            Bluetooth, BluetoothScan, BluetoothAdmin, BluetoothConnect, BluetoothAdvertise -> {
                Bluetooth(
                    activity,
                    type,
                    launcher
                ) {
                    Log.d("여기", it.message.toString())
                }
            }
            else -> {}
        }
    }


//    private var overlayResultLauncher: ActivityResultLauncher<Intent>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _binding = ActivitySplashBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        overlayResult()
//
//
//        requestOverlayPermission()
//    }
//
//    private fun requestOverlayPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            if (!Settings.canDrawOverlays(this)) {
//                try {
//                    overlayResultLauncher?.launch(
//                        Intent().apply {
//                            action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
//                            data = Uri.fromParts("package", packageName, null)
//                            putExtra("requestCode", 5)
//                        })
//                } catch (e: Exception) {
//                    overlayResultLauncher?.launch(
//                        Intent().apply {
//                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            data = Uri.fromParts("package", packageName, null)
//                            putExtra("requestCode", 5)
//                        })
//                }
//            }
//            else
//                intentMain()
//    }
//
//    private fun overlayResult() {
//        overlayResultLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    intentMain()
//                } else {
//                    requestOverlayPermission()
//                }
//            }
//    }
//
//    private fun intentMain() {
//        startActivity(Intent(this, MainActivity::class.java))
//    }
}
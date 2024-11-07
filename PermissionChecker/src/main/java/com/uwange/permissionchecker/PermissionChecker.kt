package com.uwange.permissionchecker

import android.content.Context

class PermissionChecker(
    private val context: Context
) {
    companion object {
        val a = ""
    }

    fun checker() {

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
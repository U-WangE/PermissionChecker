package com.uwange.permissionchecker.manager

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.uwange.permissionchecker.PermissionCheckerApp
import com.uwange.permissionchecker.PermissionCheckerApp.Companion.permissionCheckerPreference
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type
import com.uwange.permissionchecker.tool.BluetoothPermission
import com.uwange.permissionchecker.tool.LocationPermission
import com.uwange.permissionchecker.tool.PermissionTool

class PermissionChecker(
    private val activity: AppCompatActivity
) {
    private var launcher: ActivityResultLauncher<Array<String>>
    private var intentLauncher: ActivityResultLauncher<Intent>

    internal companion object {
        var resultLiveData: MutableLiveData<PermissionResponse>? = MutableLiveData()
    }

    private var callback: ((PermissionResponse) -> Unit)? = null

    private val resultObserver: Observer<PermissionResponse> = Observer {
        callback?.invoke(resultLiveData?.value?: PermissionResponse(false, "result error"))
    }

    private lateinit var permissionTool: PermissionTool

    private var type: Type? = null

    init {
        launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissionCheckerPreference.checkFirstTime = false
            permissionCheckerPreference.lastActionIsIntentLauncher = false

            resultLiveData?.postValue(permissionTool.checkGrant(permissions))
        }
        intentLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // intent 후 permission 상태 체크 하는 방법이 없기 때문에 해당 권한 retry
            permissionCheckerPreference.lastActionIsIntentLauncher = true

            type?.let {
                request(it)
            }?: resultLiveData?.postValue(PermissionResponse(false, "type error", type))
        }
        PermissionCheckerApp(activity).init()
    }

    fun request(type: Type) {
        this.type = type

        permissionTool = when (type) {
            is Type.BluetoothType -> {
                BluetoothPermission(activity)
            }
            is Type.LocationType -> {
                LocationPermission(activity)
            }
        }

        permissionTool.requestPermissions(type, launcher, intentLauncher)
    }

    fun result(callback: (permissionResponse: PermissionResponse) -> Unit) {
        this.callback = callback

        resultLiveData?.observe(activity, resultObserver)
    }
}
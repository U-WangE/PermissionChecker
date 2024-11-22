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

abstract class PermissionChecker(
    private val activity: AppCompatActivity
) {
    private var launcher: ActivityResultLauncher<Array<String>>
    private var intentLauncher: ActivityResultLauncher<Intent>

    internal var resultLiveData: MutableLiveData<PermissionResponse>? = MutableLiveData()

    private var callback: ((PermissionResponse) -> Unit)? = null

    private val resultObserver:  Observer<PermissionResponse> = Observer {
        callback?.invoke(resultLiveData?.value?: PermissionResponse(false, "result error"))
    }

    private var type: Type? = null

    init {
        launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissionCheckerPreference.checkFirstTime = false
            permissionCheckerPreference.lastActionIsIntentLauncher = false

            resultLiveData?.postValue(checkGrant(permissions))
        }
        intentLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // intent 후 permission 상태 체크 하는 방법이 없기 때문에 해당 권한 retry
            permissionCheckerPreference.lastActionIsIntentLauncher = true

            request(type)
        }
        PermissionCheckerApp(activity).init()
    }

    internal abstract fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse
    internal abstract fun requestPermissions(type: Type?, launcher: ActivityResultLauncher<Array<String>>, intentLauncher: ActivityResultLauncher<Intent>)

    open fun result(callback: (PermissionResponse) -> Unit) {
        this.callback = callback

        resultLiveData?.observe(activity, resultObserver)
    }

    open fun request(type: Type?) {
        this.type = type

        requestPermissions(type, launcher, intentLauncher)
    }

    open fun destroy() {
        callback = null
        resultLiveData?.removeObserver(resultObserver)
        resultLiveData = null
    }
}
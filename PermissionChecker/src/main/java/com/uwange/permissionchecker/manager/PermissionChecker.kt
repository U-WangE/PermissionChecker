package com.uwange.permissionchecker.manager

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.uwange.permissionchecker.Response
import com.uwange.permissionchecker.Type

abstract class PermissionChecker(
    private val activity: AppCompatActivity
) {
    private var launcher: ActivityResultLauncher<Array<String>>

    internal var resultLiveData: MutableLiveData<Response>? = MutableLiveData()

    private var callback: ((Response) -> Unit)? = null

    private val resultObserver:  Observer<Response> = Observer {
        callback?.invoke(resultLiveData?.value?: Response(false, ""))
    }

    init {
        launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            resultLiveData?.postValue(checkGrant(permissions))
        }
    }

    internal abstract fun checkGrant(permissions: Map<String, Boolean>): Response
    internal abstract fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>)

    open fun result(callback: (Response) -> Unit) {
        this.callback = callback

        resultLiveData?.observe(activity, resultObserver)
    }

    open fun request(type: Type) {
        requestPermissions(type, launcher)
    }

    open fun destroy() {
        callback = null
        resultLiveData?.removeObserver(resultObserver)
        resultLiveData = null
    }
}
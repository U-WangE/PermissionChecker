package com.uwange.permissionchecker.manager

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type

abstract class PermissionChecker(
    private val activity: AppCompatActivity
) {

    private var launcher: ActivityResultLauncher<Array<String>>

    internal var resultLiveData: MutableLiveData<PermissionResponse>? = MutableLiveData()

    private var callback: ((PermissionResponse) -> Unit)? = null

    private val resultObserver:  Observer<PermissionResponse> = Observer {
        callback?.invoke(resultLiveData?.value?: PermissionResponse(false, "result error"))
    }

    init {
        launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            resultLiveData?.postValue(checkGrant(permissions))
        }
    }

    internal abstract fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse
    internal abstract fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>)

    open fun result(callback: (PermissionResponse) -> Unit) {
        //TODO:: result에서 2번 이상 거절 했는지 판단
        this.callback = callback

        resultLiveData?.observe(activity, resultObserver)
    }

    open fun request(type: Type) {
        //TODO:: 시스템 권한의 경우 해당 ActivityResultContracts.RequestMultiplePermissions() 로 권한 요청 불가
        requestPermissions(type, launcher)
    }

    open fun destroy() {
        callback = null
        resultLiveData?.removeObserver(resultObserver)
        resultLiveData = null
    }
}
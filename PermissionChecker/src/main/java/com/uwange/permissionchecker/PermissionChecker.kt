package com.uwange.permissionchecker

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

enum class Type {
    Location,
    Bluetooth, BluetoothScan, BluetoothConnect, BluetoothAdvertise, BluetoothALL
}


class BluetoothPermission(private val activity: AppCompatActivity): PermissionChecker(activity) {
    private var bluetooth: Bluetooth? = null

    override fun checkGrant(permissions: Map<String, Boolean>): Response {
        return bluetooth?.checkGrant(permissions) ?: Response(false, "Bluetooth permission Denied")
    }

    override fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>) {
        if (type in listOf(Type.Bluetooth, Type.BluetoothScan, Type.BluetoothConnect, Type.BluetoothAdvertise)) {
            bluetooth = Bluetooth(type)
            bluetooth?.request(activity, launcher) {
                resultLiveData?.postValue(it)
            }
        }
    }
}

class LocationPermission(private val activity: AppCompatActivity): PermissionChecker(activity) {
    private var location: Location? = null

    override fun checkGrant(permissions: Map<String, Boolean>): Response {
        return location?.checkGrant(permissions) ?: Response(false, "Bluetooth permission Denied")
    }

    override fun requestPermissions(type: Type, launcher: ActivityResultLauncher<Array<String>>) {
        if (type in listOf(Type.Location)) {
            location = Location(type)
            location?.request(activity, launcher) {
                resultLiveData?.postValue(it)
            }
        }
    }
}


abstract class PermissionChecker(
    private val activity: AppCompatActivity
) {
    private var launcher: ActivityResultLauncher<Array<String>>

    internal var resultLiveData: MutableLiveData<Response>? = MutableLiveData()

    private var callback: ((Response) -> Unit)? = null

    private val resultObserver:  Observer<Response> = Observer {
        callback?.invoke(resultLiveData?.value?:Response(false, ""))
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
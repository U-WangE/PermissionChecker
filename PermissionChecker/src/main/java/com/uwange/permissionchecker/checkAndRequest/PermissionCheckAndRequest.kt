package com.uwange.permissionchecker.checkAndRequest

import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.uwange.permissionchecker.PermissionCheckerUtil.getDeniedPermissions
import com.uwange.permissionchecker.PermissionCheckerUtil.getGrantedPermissions
import com.uwange.permissionchecker.PermissionResponse
import com.uwange.permissionchecker.Type

abstract class PermissionCheckAndRequest(
    private val activity: Activity,
    private val type: Type
) {
    internal abstract fun getPermissions(): Array<String>
    internal abstract fun isPermissionsGranted(permissions: Map<String, Boolean>): Boolean?

    open fun request(
        launcher: ActivityResultLauncher<Array<String>>,
        callBack: (PermissionResponse) -> Unit
    ) {
        val permissions = getPermissions()
        if (permissions.isEmpty()) {
            callBack(PermissionResponse(false, "$type Permission Type Miss Match", type))
            return
        }

        val isPermissionGranted = permissions.all {
            activity.checkSelfPermission(it) == PERMISSION_GRANTED
        }

        //해당 위치에서 shouldShowRequestPermissionRationale
        // 처음 인지 판단이 필요한데 해당 판단 preference 로 처리해야 할 듯함
        // false 인 경우 처리 해야함
        if (isPermissionGranted)
            callBack(PermissionResponse(true, "$type Permission Already Granted", type, permissions.toList()))
        else
            launcher.launch((permissions))
    }

    open fun checkGrant(permissions: Map<String, Boolean>): PermissionResponse {
        val isGrantedAll = isPermissionsGranted(permissions)
        val grantedPermissions = getGrantedPermissions(permissions)
        val deniedPermissions = getDeniedPermissions(permissions)

        val isDeniedMoreThanTwice = !deniedPermissions.all {
            // shouldShowRequestPermissionRationale :
            //   명시적으로 사용자가 권한을 거부한 경우 true
            //   권한 요청이 처음이거나, 다시 묻지 않음을 선택한 경우, 권한을 허용한 경우 false
            // 해당 위치에서는 수락시 해당 코드 블럭이 실행 되지 않음.
            // 처음 거부시 true, 2번 이상 거부시 false
            shouldShowRequestPermissionRationale(activity, it)
        }

        return if (isGrantedAll == null)
            PermissionResponse(false, "$type Permission Type Miss Match", type, grantedPermissions, deniedPermissions)
        else if (isGrantedAll)
            PermissionResponse(isGrantedAll, "$type Permission Granted", type, grantedPermissions, deniedPermissions)
        else
            PermissionResponse(isGrantedAll, "$type Permission Denied", type, grantedPermissions, deniedPermissions, isDeniedMoreThanTwice)
    }
}
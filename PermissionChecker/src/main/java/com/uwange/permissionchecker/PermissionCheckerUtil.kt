package com.uwange.permissionchecker

internal object PermissionCheckerUtil {
    fun getDeniedPermissions(permissions: Map<String, Boolean>): List<String> {
        return permissions.filter { !it.value }.map { it.key }
    }

    fun getGrantedPermissions(permissions: Map<String, Boolean>): List<String> {
        return permissions.filter { it.value }.map { it.key }
    }
}
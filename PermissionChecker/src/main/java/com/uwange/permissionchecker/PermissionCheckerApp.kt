package com.uwange.permissionchecker

import android.content.Context

internal class PermissionCheckerApp(private val context: Context) {
    internal companion object {
        lateinit var permissionCheckerPreference: PermissionCheckerPreference
    }

    fun init() {
        permissionCheckerPreference = PermissionCheckerPreference(context)
    }
}
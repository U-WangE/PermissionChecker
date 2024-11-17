package com.uwange.permissionchecker

data class PermissionResponse(
    var result: Boolean,
    var message: String? = null,
    var type: Type? = null,
    var granted: List<String> = listOf(),
    var denied: List<String> = listOf(),
    var isDeniedMoreThanTwice: Boolean? = null
)

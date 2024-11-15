package com.uwange.permissionchecker

data class Response(
    var result: Boolean,
    var message: String? = null,
    var type: Type? = null
)

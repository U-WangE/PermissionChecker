package com.uwange.permissionchecker

import android.content.Context
import android.content.Context.MODE_PRIVATE

class PermissionCheckerPreference(context: Context) {
    private val pref = context.getSharedPreferences("PermissionCheckerPreference", MODE_PRIVATE)

    // 권한 요청이 최초인 경우
    var checkFirstTime: Boolean
        get() = pref.getBoolean("check_first_time", true)
        set(value) { pref.edit().putBoolean("check_first_time", value).apply() }

    // 마지막 권한 요청 Launcher 가 intent launcher 인 경우 true (설정에서 권한 수락하는 경우)
    var lastActionIsIntentLauncher: Boolean
        get() = pref.getBoolean("last_action_is_intent_launcher", false)
        set(value) { pref.edit().putBoolean("last_action_is_intent_launcher", value).apply() }
}
package com.koresuniku.wishmaster.system

import android.content.Context
import android.os.Build
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration

object DeviceUtils {

    fun deviceHasNavigationBar(context: Context): Boolean {
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }

    fun sdkIsLollipopOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= 20
    }

    fun sdkIsKitkatOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= 19
    }

    fun sdkIsMarshmallowOrHigher(): Boolean {
        return Build.VERSION.SDK_INT >= 23
    }

}
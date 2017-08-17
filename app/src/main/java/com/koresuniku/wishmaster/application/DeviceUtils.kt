package com.koresuniku.wishmaster.application

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import com.koresuniku.wishmaster.ui.UiUtils

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

    fun getDisplayWidthInDp(activity: Activity): Float {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width = size.x
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = size.y
        }
        return UiUtils.convertPixelsToDp(width.toFloat())
    }

    fun getMaximumDisplayWidthInPx(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width: Int
        if (size.x > size.y) {
            width = size.x
        } else width = size.y

        return width
    }

}
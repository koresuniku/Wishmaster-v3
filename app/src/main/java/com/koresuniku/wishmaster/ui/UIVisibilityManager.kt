package com.koresuniku.wishmaster.ui

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.WindowManager
import com.koresuniku.wishmaster.system.DeviceUtils

object UIVisibilityManager {

    fun showSystemUI(activity: Activity) {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (DeviceUtils.deviceHasNavigationBar(activity)) {
                    activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                } else {
                    activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }
    }

    fun hideSystemUI(activity: Activity) {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            activity.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE
        }
    }

    fun setBarsTranslucent(activity: Activity, translucent: Boolean) {
        setStatusBarTranslucent(activity, translucent)
        setNavigationBarTranslucent(activity, translucent)
    }

    fun setStatusBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun setNavigationBarTranslucent(activity: Activity, translucent: Boolean) {
        if (translucent) {
            //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }
}
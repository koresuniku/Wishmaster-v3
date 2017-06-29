package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UIUtils
import android.R.attr.y
import android.R.attr.x
import android.content.res.Configuration
import android.graphics.Point
import android.util.Log
import android.view.Display
import com.koresuniku.wishmaster.system.PreferenceUtils


object ImageManager {
    val LOG_TAG: String = ImageManager::class.java.simpleName

    fun computeImageWidthInDp(activity: Activity): Float {
        val paddingInDp = UIUtils.convertPixelsToDp(activity.resources.getDimension(R.dimen.post_item_side_padding))
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width = size.x
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = size.y
        }
        val widthInDp = UIUtils.convertPixelsToDp(width.toFloat())
        //Log.d(LOG_TAG, "display width in dp: " + widthInDp)
        //Log.d(LOG_TAG, "padding in dp: " + paddingInDp)
        return (widthInDp - (5 * paddingInDp)) / 4
    }

    fun getPreferredMaximumImageHeightInDp(activity: Activity): Float {
        return PreferenceUtils.getPreferredMaximumImageHeightInDp(activity)
    }
}
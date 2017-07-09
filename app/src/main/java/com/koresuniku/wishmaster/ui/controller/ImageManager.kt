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
import com.koresuniku.wishmaster.system.DeviceUtils
import com.koresuniku.wishmaster.system.PreferenceUtils


object ImageManager {
    val LOG_TAG: String = ImageManager::class.java.simpleName

    fun computeImageWidthInDp(activity: Activity, forDialog: Boolean): Float {
        val paddingInDp = UIUtils.convertPixelsToDp(activity.resources.getDimension(R.dimen.post_item_side_padding))
        val widthInDp = DeviceUtils.getDisplayWidthInDp(activity)

        var additionalPadding: Float = 0f
        if (forDialog) additionalPadding = UIUtils.convertPixelsToDp(
                activity.resources.getDimension(R.dimen.post_item_dialog_side_padding))

        return (widthInDp - (additionalPadding * 2) - (5 * paddingInDp)) / 4
    }

    fun getPreferredMaximumImageHeightInDp(activity: Activity): Float {
        return PreferenceUtils.getPreferredMaximumImageHeightInDp(activity)
    }
}
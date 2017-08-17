package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.PreferenceUtils


object ImageManager {
    val LOG_TAG: String = ImageManager::class.java.simpleName

    fun computeImageWidthInDp(activity: Activity, forDialog: Boolean): Float {
        val paddingInDp = UiUtils.convertPixelsToDp(activity.resources.getDimension(R.dimen.post_item_side_padding))
        val widthInDp = DeviceUtils.getDisplayWidthInDp(activity)

        var additionalPadding: Float = 0f
        if (forDialog) additionalPadding = UiUtils.convertPixelsToDp(
                activity.resources.getDimension(R.dimen.post_item_dialog_side_padding))

        return (widthInDp - (additionalPadding * 2) - (5 * paddingInDp)) / 4
    }

    fun getPreferredMaximumImageHeightInDp(activity: Activity): Float {
        return PreferenceUtils.getPreferredMaximumImageHeightInDp(activity)
    }

    fun getPreferredMinimumImageHeightInDp(activity: Activity): Float {
        return PreferenceUtils.getPreferredMinimumImageHeightInDp(activity)
    }
}
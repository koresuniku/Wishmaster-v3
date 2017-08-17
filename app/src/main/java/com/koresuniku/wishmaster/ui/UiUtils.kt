package com.koresuniku.wishmaster.ui

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView

object UiUtils {

    fun setImageViewColorFilter(imageView: ImageView, color: Int) {
        imageView.setColorFilter(
                imageView.resources.getColor(color),
                PorterDuff.Mode.SRC_ATOP)
    }

    fun convertPixelsToDp(px: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.round(dp).toFloat()
    }

    fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px).toFloat()
    }

}
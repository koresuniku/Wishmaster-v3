package com.koresuniku.wishmaster.ui

import android.graphics.PorterDuff
import android.widget.ImageView

object UIUtils {

    fun setImageViewColorFilter(imageView: ImageView, color: Int) {
        imageView.setColorFilter(
                imageView.resources.getColor(color),
                PorterDuff.Mode.SRC_ATOP)
    }
}
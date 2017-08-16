package com.koresuniku.wishmaster.ui.anim

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation


class ResizeAnimation(private val mView: View, private val mFromWidth: Float,
                      private val mFromHeight: Float, private val mToWidth: Float,
                      private val mToHeight: Float) : Animation() {

    init {
        duration = 300
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight
        val width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth
        val p = mView.layoutParams
        p.height = height.toInt()
        p.width = width.toInt()
        mView.requestLayout()
    }
}
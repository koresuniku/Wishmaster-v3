package com.koresuniku.wishmaster.ui.anim

import android.app.Dialog
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.WindowManager




class DialogResizeAnimation(val dialogContentView: View, val width: Int, val fromHeight: Int) : Animation() {
    val LOG_TAG: String = DialogResizeAnimation::class.java.simpleName

    init {
        Log.d(LOG_TAG, "from height: $fromHeight")
        duration = 1000
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialogContentView.window.attributes)
//        lp.height = ((1 - interpolatedTime) * fromHeight).toInt()
//        lp.width = width
//        dialogContentView.window.setAttributes(lp)

        dialogContentView.layoutParams.height = ((1 - interpolatedTime) * fromHeight).toInt()
        dialogContentView.requestLayout()
    }
}
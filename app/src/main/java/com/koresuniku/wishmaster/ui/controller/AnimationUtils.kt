package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.koresuniku.wishmaster.R

object AnimationUtils {
    val LOG_TAG: String = AnimationUtils::class.java.simpleName
    val THUMBNAIL_ANIMATION_DURATION: Long = 200L

    val alpha: AlphaAnimation = fadeOut()


    fun fadeOut(): AlphaAnimation {
        val alpha: AlphaAnimation = AlphaAnimation(0f, 1f)
        alpha.duration = 250
        return alpha
    }

//    fun resizeThumbnail(v: ImageView, activity: Activity,
//                        initialHeight: Int, finalHeight: Int): Animation {
//
//        val up = initialHeight > finalHeight
//        val difference = Math.abs(initialHeight - finalHeight)
//        val maxHeight = ImageManager.getPreferredMaximumImageHeightInDp(activity)
//
//        val a = object : Animation() {
//            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
//                val heightToAdd = (difference * interpolatedTime).toInt()
//                if (up) {
//                    v.layoutParams.height = initialHeight - heightToAdd
//                } else {
//                    if (initialHeight + heightToAdd <= maxHeight) {
//                        v.layoutParams.height = initialHeight + heightToAdd
//                    }
//                }
//                v.requestLayout()
//                Log.d(LOG_TAG, "appliyning transformation")
//            }
//
//            override fun willChangeBounds(): Boolean {
//                return true
//            }
//        }
//        a.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationStart(animation: Animation) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animation) {
//                v.requestLayout()
//            }
//
//            override fun onAnimationRepeat(animation: Animation) {
//
//            }
//        })
//        a.duration = THUMBNAIL_ANIMATION_DURATION
//        return a
//    }
}
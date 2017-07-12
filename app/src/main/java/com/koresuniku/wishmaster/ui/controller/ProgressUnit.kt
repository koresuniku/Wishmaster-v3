package com.koresuniku.wishmaster.ui.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.PreferenceUtils
import com.koresuniku.wishmaster.ui.view.ProgressView
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource

class ProgressUnit(val mView: ProgressView) {

    var mLoadingLayout: ViewGroup? = null

    fun showProgressYoba() {

        val loadingPref: String = PreferenceUtils.getSharedPreferences(mView.getActivityOverridden())
                        .getString(mView.getActivityOverridden().getString(R.string.pref_loading_view_key),
                                mView.getActivityOverridden().getString(R.string.pref_loading_yoba_default))
        var isImageViewRotating: Boolean = false

        when (loadingPref) {
            mView.getActivityOverridden().getString(R.string.pref_loading_yoba_default) -> {
                mLoadingLayout = LayoutInflater.from(mView.getActivityOverridden())
                        .inflate(R.layout.loading_rotating_imageview, null, false) as ViewGroup
                mLoadingLayout!!.find<ImageView>(R.id.progress_container).imageResource = R.drawable.yoba_default
                (mView.getProgressContainer() as ViewGroup).addView(mLoadingLayout)
                isImageViewRotating = true
            }
            mView.getActivityOverridden().getString(R.string.pref_loading_spinner) -> {
                mLoadingLayout = LayoutInflater.from(mView.getActivityOverridden())
                        .inflate(R.layout.loading_rotating_imageview, null, false) as ViewGroup
                mLoadingLayout!!.find<ImageView>(R.id.progress_container).imageResource = R.drawable.spinner_default
                (mView.getProgressContainer() as ViewGroup).addView(mLoadingLayout)
                isImageViewRotating = true
            }
            mView.getActivityOverridden().getString(R.string.pref_loading_android_progress_bar) -> {
                mLoadingLayout = LayoutInflater.from(mView.getActivityOverridden())
                        .inflate(R.layout.loading_rotating_progress_bar, null, false) as ViewGroup
                (mView.getProgressContainer() as ViewGroup).addView(mLoadingLayout)
                isImageViewRotating = false
            }
        }
        mView.getProgressContainer().post {
            mView.getProgressContainer().visibility = View.VISIBLE

            val runnable = object : Runnable {
                override fun run() {
                    mView.getProgressContainer().findViewById(R.id.progress_container).animate().rotationBy(360f)
                            .withEndAction(this).setDuration(1000).setInterpolator(LinearInterpolator()).start()
                }
            }
            if (isImageViewRotating) {
                mView.getProgressContainer().findViewById(R.id.progress_container).animate().rotationBy(360f)
                        .withEndAction(runnable).setDuration(1000).setInterpolator(LinearInterpolator()).start()
            }
        }
    }

    fun hideProgressYoba() {
            mView.getProgressContainer().post { mView.getProgressContainer().visibility = View.GONE }
            if (mView.getProgressContainer().findViewById(R.id.progress_container) != null) {
                mView.getProgressContainer().findViewById(R.id.progress_container).clearAnimation()
            }
    }
}
package com.koresuniku.wishmaster.ui.controller

import android.app.Fragment
import android.content.Context
import android.support.annotation.IdRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.PreferenceUtils
import org.jetbrains.anko.find
import org.jetbrains.anko.imageResource

class ProgressUnit(private val mContext: Context, private val mProgressContainer: ViewGroup) {

    private lateinit var mLoadingLayout: ViewGroup

    fun showProgressYoba() {
        val loadingPref: String = PreferenceUtils.getSharedPreferences(mContext)
                        .getString(mContext.getString(R.string.pref_loading_view_key),
                                mContext.getString(R.string.pref_loading_yoba_default))
        var isImageViewRotating = false

        when (loadingPref) {
            mContext.getString(R.string.pref_loading_yoba_default) -> {
                mLoadingLayout = LayoutInflater.from(mContext)
                        .inflate(R.layout.loading_rotating_imageview, null, false) as ViewGroup
                mLoadingLayout.find<ImageView>(R.id.progress_container).imageResource = R.drawable.yoba_default
                mProgressContainer.addView(mLoadingLayout)
                isImageViewRotating = true
            }
            mContext.getString(R.string.pref_loading_spinner) -> {
                mLoadingLayout = LayoutInflater.from(mContext)
                        .inflate(R.layout.loading_rotating_imageview, null, false) as ViewGroup
                mLoadingLayout.find<ImageView>(R.id.progress_container).imageResource = R.drawable.spinner_default
                mProgressContainer.addView(mLoadingLayout)
                isImageViewRotating = true
            }
            mContext.getString(R.string.pref_loading_android_progress_bar) -> {
                mLoadingLayout = LayoutInflater.from(mContext)
                        .inflate(R.layout.loading_rotating_progress_bar, null, false) as ViewGroup
                mProgressContainer.addView(mLoadingLayout)
                isImageViewRotating = false
            }
        }
        mProgressContainer.post {
            mProgressContainer.visibility = View.VISIBLE

            val runnable = object : Runnable {
                override fun run() {
                    mProgressContainer.findViewById(R.id.progress_container).animate().rotationBy(360f)
                            .withEndAction(this).setDuration(1000).setInterpolator(LinearInterpolator()).start()
                }
            }
            if (isImageViewRotating) {
                mProgressContainer.findViewById(R.id.progress_container).animate().rotationBy(360f)
                        .withEndAction(runnable).setDuration(1000).setInterpolator(LinearInterpolator()).start()
            }
        }
    }

    fun hideProgressYoba() {
           mProgressContainer.post { mProgressContainer.visibility = View.GONE }
            if (mProgressContainer.findViewById(R.id.progress_container) != null) {
                mProgressContainer.findViewById(R.id.progress_container).clearAnimation()
            }
    }
}
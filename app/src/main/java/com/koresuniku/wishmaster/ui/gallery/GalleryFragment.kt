package com.koresuniku.wishmaster.ui.gallery


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.system.App
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats

class GalleryFragment() :
        Fragment(), SoundVolumeChangeListener, UIVisibilityManager.UiVisibilityChangedCallback {
    val LOG_TAG: String = GalleryFragment::class.java.simpleName

    var mPagerAdapterView: GalleryPagerAdapterView? = null
    var mAdapter: GalleryPagerAdapter? = null
    var mPosition: Int? = null
    var mFile: Files? = null

    var mRootView: ViewGroup? = null

    var mGalleryImageUnit: GalleryImageUnit? = null
    var mGalleryGifUnit: GalleryGifUnit? = null
    var mGalleryVideoUnit: GalleryVideoUnit? = null

    var animCollapseActionBar: ScaleAnimation? = null
    var animExpandActionBar: ScaleAnimation? = null

    constructor(pagerAdapterView: GalleryPagerAdapterView, position: Int) : this() {
        mPagerAdapterView = pagerAdapterView
        mAdapter = pagerAdapterView.getAdapter()
        mPosition = position
        mFile = mAdapter!!.mFiles[position]

        setupAnimations()
    }

    fun setupAnimations() {
        animExpandActionBar = ScaleAnimation(
                1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f)
        animExpandActionBar!!.duration = 250
        animCollapseActionBar = ScaleAnimation(
                1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f)
        animCollapseActionBar!!.duration = 250
        animExpandActionBar!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {
                mPagerAdapterView!!.getGalleryActionBar()
                        .mActivityToolbarContainer.visibility = View.VISIBLE
            }
        })
        animCollapseActionBar!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                mPagerAdapterView!!.getGalleryActionBar()
                    .mActivityToolbarContainer.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = FrameLayout(container!!.context)

        val format: String = TextUtils.getSubstringAfterDot(mFile!!.getPath())
        Log.d(LOG_TAG, "format: $format")

        if (Formats.IMAGE_FORMATS.contains(format)) {
            mGalleryImageUnit = GalleryImageUnit(this, mFile!!)
        }
        if (Formats.GIF_FORMAT.contains(format)) {
            mGalleryGifUnit = GalleryGifUnit(this, mFile!!)
        }
        if (Formats.VIDEO_FORMATS.contains(format)) {
            mGalleryVideoUnit = GalleryVideoUnit(this, mFile!!)
            App.mSoundContentObserver!!.bindListener(this)
        }

        return mRootView
    }

    fun isCurrentPosition(): Boolean {
        return mAdapter!!.currentPosition == mPosition
    }

    fun onClick() {
        UIVisibilityManager.changeSystemUiVisibility(this)
    }

    override fun onUiVisibilityChanged(isShown: Boolean, delegateToOtherFragments: Boolean) {
        if (!isShown) mPagerAdapterView!!.getGalleryActionBar()
                .mActivityToolbarContainer.startAnimation(animCollapseActionBar)
        else mPagerAdapterView!!.getGalleryActionBar()
                .mActivityToolbarContainer.startAnimation(animExpandActionBar)

        if (mGalleryVideoUnit != null) mGalleryVideoUnit!!.onUiVisibilityChanged(isShown)

        if (delegateToOtherFragments) mPagerAdapterView!!.onUiVisibilityChanged(isShown, mPosition!!)
    }


    override fun onVolumeChanged(volume: Int) {
        mGalleryVideoUnit!!.onSoundChanged(volume)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (mGalleryVideoUnit != null) mGalleryVideoUnit!!.onConfigurationChanged(newConfig!!)
    }

    fun onBackPressed() {
        if (mGalleryVideoUnit != null) mGalleryVideoUnit!!.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        if (mGalleryVideoUnit != null) mGalleryVideoUnit!!.pauseVideoView()
    }

    fun onDestroyItem() {
        if (mGalleryVideoUnit != null) {
            App.mSoundContentObserver!!.unbindListener(this)
            mGalleryVideoUnit!!.onItemDestroy()
        }

    }
}

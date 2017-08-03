package com.koresuniku.wishmaster.ui.gallery


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.system.App
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats

class GalleryFragment() : Fragment(), SoundVolumeChangeListener {
    val LOG_TAG: String = GalleryFragment::class.java.simpleName

    var mAdapter: GalleryPagerAdapter? = null
    var mPosition: Int? = null
    var mFile: Files? = null

    var mRootView: ViewGroup? = null

    var mGalleryImageUnit: GalleryImageUnit? = null
    var mGalleryGifUnit: GalleryGifUnit? = null
    var mGalleryVideoUnit: GalleryVideoUnit? = null

    constructor(adapter: GalleryPagerAdapter, position: Int) : this() {
        mAdapter = adapter
        mPosition = position
        mFile = adapter.mFiles[position]
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

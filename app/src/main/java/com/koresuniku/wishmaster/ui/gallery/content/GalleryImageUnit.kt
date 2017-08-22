package com.koresuniku.wishmaster.ui.gallery.content

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.piasy.biv.view.BigImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import org.jetbrains.anko.find
import org.jetbrains.anko.layoutInflater
import java.lang.Exception

class GalleryImageUnit(val mFragment: GalleryFragment, val file: Files) : View.OnClickListener {
    val LOG_TAG: String = GalleryImageUnit::class.java.simpleName

    var mImageLayout: ViewGroup? = null
    var mClickableLayout: FrameLayout? = null
    var mProgressBar: ProgressBar? = null
    var mBigImage: BigImageView? = null

    init {
        onCreate()
    }

    fun onCreate() {
        mImageLayout = mFragment.context.layoutInflater.inflate(
                R.layout.gallery_image_layout, FrameLayout(mFragment.context), false) as ViewGroup
        mProgressBar = mImageLayout!!.find(R.id.progressBar)
        mClickableLayout = mImageLayout!!.find(R.id.gallery_image_clickable_layout)
        mBigImage = mImageLayout!!.find(R.id.mBigImage)

        mClickableLayout!!.setOnClickListener(this)
        mClickableLayout!!.bringToFront()
        mBigImage!!.setOnClickListener(this)
        mBigImage!!.showImage(Uri.parse(Dvach.DVACH_BASE_URL + file.getPath()))
        mBigImage!!.ssiv.setOnImageEventListener(OnBigImageEventListener())
        mBigImage!!.ssiv.maxScale = 10.0f
        mBigImage!!.ssiv.recycle()

        mFragment.mRootView!!.addView(mImageLayout)
    }

    override fun onClick(p0: View?) {
        Log.d(LOG_TAG, "onClickNoSpoilersOrLinksFound:")
        mFragment.onClick()
    }

    fun onBackPressed() {
        mBigImage = null
    }

    inner class OnBigImageEventListener : SubsamplingScaleImageView.OnImageEventListener {
        override fun onImageLoaded() {
            mProgressBar!!.visibility = View.GONE
            mBigImage!!.bringToFront()
        }

        override fun onReady() {

        }

        override fun onTileLoadError(p0: Exception?) {

        }

        override fun onPreviewReleased() {

        }

        override fun onImageLoadError(p0: Exception?) {

        }

        override fun onPreviewLoadError(p0: Exception?) {

        }
    }
}
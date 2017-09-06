package com.koresuniku.wishmaster.ui.gallery.content

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.domain.Dvach
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import org.jetbrains.anko.find
import org.jetbrains.anko.layoutInflater

class GalleryGifUnit(val mFragment: GalleryFragment, val file: Files) : View.OnClickListener {
    val LOG_TAG: String = GalleryImageUnit::class.java.simpleName

    var mGifImageLayout: ViewGroup? = null
    var mProgressBar: ProgressBar? = null
    var mGifImage: ImageView? = null

    init {
        onCreate()
    }

    fun onCreate() {
        mGifImageLayout = mFragment.context.layoutInflater.inflate(
                R.layout.gallery_gif_layout, FrameLayout(mFragment.context), false) as ViewGroup
        mProgressBar = mGifImageLayout!!.find(R.id.progressBar)
        mGifImage = mGifImageLayout!!.find(R.id.gif_image)

        mGifImageLayout!!.setOnClickListener(this)
        mGifImage!!.setOnClickListener(this)
        Glide.with(mFragment.context).load(Uri.parse(Dvach.DVACH_BASE_URL + file.getPath()))
                .asGif().diskCacheStrategy(DiskCacheStrategy.NONE).listener(object : RequestListener<Uri, GifDrawable> {
            override fun onException(e: Exception, uri: Uri, target: Target<GifDrawable>,
                                     b: Boolean): Boolean {

                return false
            }

            override fun onResourceReady(gifDrawable: GifDrawable, uri: Uri,
                                         target: Target<GifDrawable>, b: Boolean,
                                         b1: Boolean): Boolean {
                mProgressBar!!.visibility = View.GONE
                return false
            }
        }).into(mGifImage)
        mFragment.mRootView!!.addView(mGifImageLayout)
    }

    override fun onClick(p0: View?) {
        Log.d(LOG_TAG, "onClickNoSpoilersOrLinksFound:")
        mFragment.onClick()
    }
}
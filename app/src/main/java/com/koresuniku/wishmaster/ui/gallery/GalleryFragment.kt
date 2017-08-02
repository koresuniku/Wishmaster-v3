package com.koresuniku.wishmaster.ui.gallery


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource

class GalleryFragment() : Fragment() {
    val LOG_TAG: String = GalleryFragment::class.java.simpleName

    var mFile: Files? = null

    var mRootView: ViewGroup? = null

    var mGalleryImageUnit: GalleryImageUnit? = null
    var mGalleryGifUnit: GalleryGifUnit? = null

    constructor(file: Files) : this() {
        mFile = file
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

        return mRootView
    }

    fun onBackPressed() {

    }
}

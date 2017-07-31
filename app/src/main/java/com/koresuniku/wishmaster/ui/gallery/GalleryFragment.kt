package com.koresuniku.wishmaster.ui.gallery


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.koresuniku.wishmaster.R
import org.jetbrains.anko.image
import org.jetbrains.anko.imageResource

class GalleryFragment : Fragment() {
    val LOG_TAG: String = GalleryFragment::class.java.simpleName

    var mRootView: ViewGroup? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = FrameLayout(container!!.context)
        val image = ImageView(container.context)
        image.imageResource = R.drawable.yoba_default
        mRootView!!.addView(image)
        return mRootView
    }
}

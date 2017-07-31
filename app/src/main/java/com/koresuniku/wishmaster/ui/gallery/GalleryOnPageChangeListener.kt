package com.koresuniku.wishmaster.ui.gallery

import android.support.v4.view.ViewPager
import android.util.Log
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder


class GalleryOnPageChangeListener(val filesListViewViewHolder: FilesListViewViewHolder) :
        ViewPager.OnPageChangeListener {
    val LOG_TAG: String = GalleryOnPageChangeListener::class.java.simpleName


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        Log.d(LOG_TAG, "onPageSelected: $position")
        filesListViewViewHolder.onPageChanged(position)
    }
}
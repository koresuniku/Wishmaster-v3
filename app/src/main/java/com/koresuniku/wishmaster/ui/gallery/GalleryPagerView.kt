package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.support.v4.view.ViewPager

interface GalleryPagerView {
    fun getViewPager(): ViewPager

    fun getActivity(): Activity

    fun getGalleryActionBar(): GalleryActionBarUnit
}
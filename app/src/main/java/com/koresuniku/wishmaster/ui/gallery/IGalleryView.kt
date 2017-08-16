package com.koresuniku.wishmaster.ui.gallery

import android.content.res.Configuration
import android.support.v4.view.ViewPager
import android.view.ViewGroup
import com.koresuniku.wishmaster.http.thread_list_api.model.Files

interface IGalleryView : GalleryPagerView {
    var mGalleryLayoutContainer: ViewGroup
    var mGalleryActionBarUnit: GalleryActionBarUnit
    var mGalleryPager: ViewPager
    var mGalleryPagerAdapter: GalleryPagerAdapter?
    var mGalleryOnPageChangeListener: GalleryOnPageChangeListener?

    fun showImageOrVideo(filesList: List<Files>, file: Files)

    fun onPageChanged(newPosition: Int)

    fun onBackPressed(): Boolean

    fun onConfigurationChanged(configuration: Configuration)

}
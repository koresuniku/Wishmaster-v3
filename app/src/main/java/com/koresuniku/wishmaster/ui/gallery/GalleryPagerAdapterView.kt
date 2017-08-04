package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import com.koresuniku.wishmaster.ui.UIVisibilityManager

interface GalleryPagerAdapterView {
    fun getAdapter(): GalleryPagerAdapter

    fun getGalleryActionBar(): GalleryActionBarUnit

    fun onUiVisibilityChanged(isShown: Boolean, position: Int)
}
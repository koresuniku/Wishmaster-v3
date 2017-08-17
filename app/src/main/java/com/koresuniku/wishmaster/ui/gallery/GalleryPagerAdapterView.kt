package com.koresuniku.wishmaster.ui.gallery

interface GalleryPagerAdapterView {
    fun getAdapter(): GalleryPagerAdapter

    fun getGalleryActionBar(): GalleryActionBarUnit

    fun onUiVisibilityChanged(isShown: Boolean, position: Int)
}
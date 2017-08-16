package com.koresuniku.wishmaster.ui.gallery

import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter

class GalleryPresenter(val mSingleThreadListViewAdapter: SingleThreadListViewAdapter) {
    val LOG_TAG: String = GalleryPresenter::class.java.simpleName

    val mGalleryView: IGalleryView = GalleryViewImpl(mSingleThreadListViewAdapter)

}
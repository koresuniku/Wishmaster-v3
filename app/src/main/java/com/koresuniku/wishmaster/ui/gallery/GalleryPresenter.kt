package com.koresuniku.wishmaster.ui.gallery

import android.content.res.Configuration
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView

class GalleryPresenter(val mView: ActionBarView) {
    val LOG_TAG: String = GalleryPresenter::class.java.simpleName

    private val mGalleryView: IGalleryView = GalleryViewImpl(mView)

    fun showImageOrVideo(filesList: List<Files>, file: Files) {
        mGalleryView.showImageOrVideo(filesList, file)
    }

    fun onBackPressed(): Boolean = mGalleryView.onBackPressed()

    fun onConfigurationChanged(configuration: Configuration) {
        mGalleryView.onConfigurationChanged(configuration)
    }

}
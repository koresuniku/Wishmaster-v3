package com.koresuniku.wishmaster.ui.gallery

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView

class GalleryUnit(private val mAppCompatActivity: AppCompatActivity) {
    val LOG_TAG: String = GalleryUnit::class.java.simpleName

    private val mGalleryView: IGalleryView = GalleryViewImpl(mAppCompatActivity)

    fun showImageOrVideo(filesList: List<Files>, file: Files) {
        mGalleryView.showImageOrVideo(filesList, file)
    }

    fun onBackPressed(): Boolean = mGalleryView.onBackPressed()

    fun onConfigurationChanged(configuration: Configuration) {
        mGalleryView.onConfigurationChanged(configuration)
    }

}
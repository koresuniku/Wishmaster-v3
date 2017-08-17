package com.koresuniku.wishmaster.ui.gallery

import android.content.res.Configuration
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.ui.controller.ActionBarUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.text.TextUtils
import org.jetbrains.anko.configuration
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find

class GalleryActionBarUnit(mView: ActionBarView) : ActionBarUnit(mView, true, false) {
    override var LOG_TAG: String = GalleryActionBarUnit::class.java.simpleName

    var mBackButton: ImageView? = null
    var mTitle: TextView? = null
    var mSubtitle: TextView? = null

    var mFile: Files? = null
    var mIndexOfFile: Int? = null
    var mFilesCount: Int? = null
    var title: String? = null
    var subtitle: String? = null

    override fun postSetupActionBar() {
        if (DeviceUtils.sdkIsKitkatOrHigher() &&
                DeviceUtils.deviceHasNavigationBar(mView.getAppCompatActivity())) {
            if (mView.getAppCompatActivity().configuration.orientation ==
                    Configuration.ORIENTATION_LANDSCAPE)
                doMarginActionBar()
            else unMarginActionBar()
        }

        mBackButton = mToolbar!!.find(R.id.back_button)
        mBackButton!!.setOnClickListener { mView.onBackPressedOverridden() }
        mBackButton!!.bringToFront()
    }

    override fun getIdRes(): Int {
        return R.layout.gallery_action_bar_layout
    }

    fun onPageChanged(file: Files, indexOfFile: Int, filesCount: Int) {
        setupTitleAndSubtitle(file, indexOfFile, filesCount)
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
        if (DeviceUtils.sdkIsKitkatOrHigher() &&
                DeviceUtils.deviceHasNavigationBar(mView.getAppCompatActivity())) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                doMarginActionBar()
            else unMarginActionBar()
        }
    }

    fun doMarginActionBar() {
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                mActivityToolbarContainer.layoutParams.width,
                mActivityToolbarContainer.layoutParams.height)
        params.setMargins(0, 0, UiUtils.convertDpToPixel(
                mActivityToolbarContainer.dimen(R.dimen.navigation_bar_size).toFloat()).toInt() / 2, 0)
        mActivityToolbarContainer.layoutParams = params
    }

    fun unMarginActionBar() {
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                mActivityToolbarContainer.layoutParams.width,
                mActivityToolbarContainer.layoutParams.height)
        params.setMargins(0, 0, 0, 0)
        mActivityToolbarContainer.layoutParams = params
    }

    override fun setSupportActionBarAndTitle() {
        //Do nothing
    }

    fun setupTitleAndSubtitle(file: Files, indexOfFile: Int, filesCount: Int) {
        mTitle = mToolbar!!.find(R.id.title)
        mSubtitle = mToolbar!!.find(R.id.subtitle)

        mFile = file
        mIndexOfFile = indexOfFile
        mFilesCount = filesCount

        title = if (file.getDisplayName().isNullOrEmpty())
                    mView.getAppCompatActivity().getString(R.string.unknown)
                else file.getDisplayName()
        subtitle = TextUtils.getSubtitleStringForGalleryToolbar(
                mFile!!, mIndexOfFile!!, mFilesCount!!)

        mTitle!!.text = title
        mSubtitle!!.text = subtitle
    }

}
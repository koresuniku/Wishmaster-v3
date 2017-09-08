package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.content.res.Configuration
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.UiUtils
import com.koresuniku.wishmaster.ui.text.TextUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.configuration
import org.jetbrains.anko.dimen
import org.jetbrains.anko.find

class GalleryActionBarUnit(private val mActivity: Activity, private val mGalleryView: IGalleryView) {
    var LOG_TAG: String = GalleryActionBarUnit::class.java.simpleName

    var mActivityToolbarContainer: ViewGroup =
            mActivity.findViewById(R.id.gallery_toolbar_container) as ViewGroup
    private lateinit var mLocalToolbarContainer: ViewGroup
    private lateinit var mToolbar: Toolbar

    var mBackButton: ImageView? = null
    var mTitle: TextView? = null
    var mSubtitle: TextView? = null

    var mFile: Files? = null
    var mIndexOfFile: Int? = null
    var mFilesCount: Int? = null
    var title: String? = null
    var subtitle: String? = null

    init { EventBus.getDefault().register(this) }

    fun setupActionBar() {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mActivity.layoutInflater
                .inflate(R.layout.gallery_action_bar_layout, null, false) as RelativeLayout
        mToolbar = mLocalToolbarContainer.findViewById(R.id.toolbar) as Toolbar
        val height: Int = if (mActivity.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mActivity.resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            mActivity.resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar.layoutParams!!.height = height
        mToolbar.layoutParams!!.width = Toolbar.LayoutParams.MATCH_PARENT
        if (DeviceUtils.sdkIsKitkatOrHigher()) { mLocalToolbarContainer.setPadding(
                0, mActivity.resources.getDimension(R.dimen.status_bar_height).toInt(), 0, 0)
        }

        mActivityToolbarContainer.addView(mLocalToolbarContainer)
        if (DeviceUtils.sdkIsKitkatOrHigher() && DeviceUtils.deviceHasNavigationBar(mActivity)) {
            if (mActivity.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                doMarginActionBar()
            } else unmarginActionBar()
        }

        mBackButton = mToolbar.find(R.id.back_button)
        mBackButton!!.setOnClickListener { mGalleryView.onBackPressed() }
        mBackButton!!.bringToFront()
    }

    fun onPageChanged(file: Files, indexOfFile: Int, filesCount: Int) {
        setupTitleAndSubtitle(file, indexOfFile, filesCount)
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    fun onLifecyclerEvent(event: LifecycleEvent) {
        when(event.anEvent) {
            LifecycleEvent.ON_CONFIGURATION_CHANGED -> {
                val configuration = event.configuration
                setupActionBar()
                if (DeviceUtils.sdkIsKitkatOrHigher() && DeviceUtils.deviceHasNavigationBar(mActivity)) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) doMarginActionBar()
                else unmarginActionBar()
                }
            }
            LifecycleEvent.ON_START -> {
                EventBus.getDefault().register(this)
            }
            LifecycleEvent.ON_STOP -> {
                EventBus.getDefault().unregister(this)
            }
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

    fun unmarginActionBar() {
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                mActivityToolbarContainer.layoutParams.width,
                mActivityToolbarContainer.layoutParams.height)
        params.setMargins(0, 0, 0, 0)
        mActivityToolbarContainer.layoutParams = params
    }

    fun setupTitleAndSubtitle(file: Files, indexOfFile: Int, filesCount: Int) {
        mTitle = mToolbar.find(R.id.title)
        mSubtitle = mToolbar.find(R.id.subtitle)

        mFile = file
        mIndexOfFile = indexOfFile
        mFilesCount = filesCount

        title = if (file.getDisplayName().isNullOrEmpty()) mActivity.getString(R.string.unknown)
                else file.getDisplayName()
        subtitle = TextUtils.getSubtitleStringForGalleryToolbar(
                mFile!!, mIndexOfFile!!, mFilesCount!!)

        mTitle!!.text = title
        mSubtitle!!.text = subtitle
    }

}
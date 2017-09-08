package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.content.res.Configuration
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.domain.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UiVisibilityManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find

class GalleryViewImpl(private val mAppCompatActivity: AppCompatActivity) : IGalleryView {

    override var mGalleryLayoutContainer: ViewGroup = mAppCompatActivity.find(R.id.gallery_layout_container)
    override var mGalleryActionBarUnit = GalleryActionBarUnit(mAppCompatActivity, this)
    override var mGalleryPager: ViewPager = mAppCompatActivity.find(R.id.gallery_pager)

    override var mGalleryPagerAdapter: GalleryPagerAdapter? = null
    override var mGalleryOnPageChangeListener: GalleryOnPageChangeListener? = null
    var filesList: List<Files>? = null

    init { EventBus.getDefault().register(this) }

    override fun showImageOrVideo(filesList: List<Files>, file: Files) {
        Log.d(GalleryViewImpl::class.java.simpleName, "showImageOrVideo:")
        EventBus.getDefault().post(GalleryEvent(true))

        this.filesList = filesList

        UiVisibilityManager.setBarsTranslucent(mAppCompatActivity, true)
        mGalleryLayoutContainer.visibility = View.VISIBLE

        val currentPosition = filesList.indexOf(file)

        mGalleryPagerAdapter = GalleryPagerAdapter(
                mAppCompatActivity.supportFragmentManager, this,
                filesList, currentPosition)
        mGalleryPager.adapter = mGalleryPagerAdapter
        mGalleryPager.offscreenPageLimit = 1
        mGalleryPager.currentItem = currentPosition
        mGalleryOnPageChangeListener = GalleryOnPageChangeListener(this)
        mGalleryPager.addOnPageChangeListener(mGalleryOnPageChangeListener)

        mGalleryActionBarUnit.setupActionBar()
        mGalleryActionBarUnit.setupTitleAndSubtitle(file, currentPosition, filesList.count())
    }

    override fun onPageChanged(newPosition: Int) {
        mGalleryPagerAdapter!!.onPageChanged(newPosition)
        val file = filesList!![newPosition]
        mGalleryActionBarUnit.onPageChanged(file, newPosition, filesList!!.count())
    }

    override fun onBackPressed(): Boolean {
        return if (mGalleryLayoutContainer.visibility == View.VISIBLE) {
            Log.d(GalleryViewImpl::class.java.simpleName, "closing gallery:")
            EventBus.getDefault().post(GalleryEvent(false))
            UiVisibilityManager.setBarsTranslucent(mAppCompatActivity, false)
            mGalleryLayoutContainer.visibility = View.GONE

            mGalleryPager.clearOnPageChangeListeners()
            mGalleryPagerAdapter!!.onBackPressed()
            false
        } else true
    }

    override fun onConfigurationChanged(configuration: Configuration) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecyclerEvent(event: LifecycleEvent) {
        when(event.anEvent) {
            LifecycleEvent.ON_CONFIGURATION_CHANGED -> {
                if (mGalleryLayoutContainer.visibility == View.VISIBLE) {
                    mGalleryActionBarUnit.setupTitleAndSubtitle(mGalleryActionBarUnit.mFile!!,
                            mGalleryActionBarUnit.mIndexOfFile!!, mGalleryActionBarUnit.mFilesCount!!)
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

    override fun getViewPager(): ViewPager = mGalleryPager

    override fun getActivity(): Activity = mAppCompatActivity

    override fun onGalleryHidden() {}

    override fun getGalleryActionBar(): GalleryActionBarUnit = mGalleryActionBarUnit
}
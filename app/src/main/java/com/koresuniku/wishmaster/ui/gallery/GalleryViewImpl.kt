package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.content.res.Configuration
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter
import org.jetbrains.anko.find
import java.io.File

class GalleryViewImpl(val mView: SingleThreadListViewAdapter) : IGalleryView {
    override var mGalleryLayoutContainer: ViewGroup
        get() = mView.getActivity().find(R.id.gallery_layout_container)
        set(value) {}
    override var mGalleryActionBarUnit: GalleryActionBarUnit
        get() = GalleryActionBarUnit(mView)
        set(value) {}
    override var mGalleryPager: ViewPager
        get() = mView.getActivity().find(R.id.gallery_pager)
        set(value) {}
    override var mGalleryPagerAdapter: GalleryPagerAdapter?
        get() = null
        set(value) {}
    override var mGalleryOnPageChangeListener: GalleryOnPageChangeListener?
        get() = null
        set(value) {}
    lateinit var filesList: List<Files>

    override fun showImageOrVideo(filesList: List<Files>, file: Files) {
        this.filesList = filesList

        UIVisibilityManager.setBarsTranslucent(mView.getActivity(), true)
        mGalleryLayoutContainer.visibility = View.VISIBLE

        val currentPosition = filesList.indexOf(file)
        mGalleryActionBarUnit.setupTitleAndSubtitle(file, currentPosition, filesList.count())

        mGalleryPagerAdapter = GalleryPagerAdapter(
                mView.getAppCompatActivity().supportFragmentManager, this,
                filesList, currentPosition)
        mGalleryPager.adapter = mGalleryPagerAdapter
        mGalleryPager.offscreenPageLimit = 1
        mGalleryPager.currentItem = currentPosition
        mGalleryOnPageChangeListener = GalleryOnPageChangeListener(this)
        mGalleryPager.addOnPageChangeListener(mGalleryOnPageChangeListener)
    }

    override fun onPageChanged(newPosition: Int) {
        mGalleryPagerAdapter!!.onPageChanged(newPosition)
        val file = filesList[newPosition]
        mGalleryActionBarUnit.onPageChanged(file, newPosition, filesList.count())
    }

    override fun onBackPressed(): Boolean {
        if (mGalleryLayoutContainer.visibility == View.VISIBLE) {
            UIVisibilityManager.setBarsTranslucent(mView.getActivity(), false)
            mGalleryLayoutContainer.visibility = View.GONE

            mGalleryPager.clearOnPageChangeListeners()
            mGalleryPagerAdapter!!.onBackPressed()
            return true
        }
        return false
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        if (mGalleryLayoutContainer.visibility == View.VISIBLE) {
            mGalleryActionBarUnit.onConfigurationChanged(configuration)
            mGalleryActionBarUnit.setupTitleAndSubtitle(mGalleryActionBarUnit.mFile!!,
                    mGalleryActionBarUnit.mIndexOfFile!!, mGalleryActionBarUnit.mFilesCount!!)
        }
    }

    override fun getViewPager(): ViewPager = mGalleryPager

    override fun getActivity(): Activity = mView.getActivity()

    override fun onGalleryHidden() {}

    override fun getGalleryActionBar(): GalleryActionBarUnit = mGalleryActionBarUnit
}
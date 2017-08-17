package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.content.res.Configuration
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter
import org.jetbrains.anko.find

class GalleryViewImpl(val mView: ActionBarView) : IGalleryView {
    override var mGalleryLayoutContainer: ViewGroup = mView.getAppCompatActivity().find(R.id.gallery_layout_container)

    override var mGalleryActionBarUnit: GalleryActionBarUnit = GalleryActionBarUnit(mView)

    override var mGalleryPager: ViewPager = mView.getAppCompatActivity().find(R.id.gallery_pager)

    override var mGalleryPagerAdapter: GalleryPagerAdapter? = null
    override var mGalleryOnPageChangeListener: GalleryOnPageChangeListener? = null
    var filesList: List<Files>? = null

    override fun showImageOrVideo(filesList: List<Files>, file: Files) {
        this.filesList = filesList

        UIVisibilityManager.setBarsTranslucent(mView.getAppCompatActivity(), true)
        mGalleryLayoutContainer.visibility = View.VISIBLE

        val currentPosition = filesList.indexOf(file)

        mGalleryPagerAdapter = GalleryPagerAdapter(
                mView.getAppCompatActivity().supportFragmentManager, this,
                filesList, currentPosition)
        mGalleryPager.adapter = mGalleryPagerAdapter
        mGalleryPager.offscreenPageLimit = 1
        mGalleryPager.currentItem = currentPosition
        mGalleryOnPageChangeListener = GalleryOnPageChangeListener(this)
        mGalleryPager.addOnPageChangeListener(mGalleryOnPageChangeListener)

        mGalleryActionBarUnit.setupActionBar(mView.getAppCompatActivity().resources.configuration)
        mGalleryActionBarUnit.setupTitleAndSubtitle(file, currentPosition, filesList.count())
    }

    override fun onPageChanged(newPosition: Int) {
        mGalleryPagerAdapter!!.onPageChanged(newPosition)
        val file = filesList!![newPosition]
        mGalleryActionBarUnit.onPageChanged(file, newPosition, filesList!!.count())
    }

    override fun onBackPressed(): Boolean {
        if (mGalleryLayoutContainer.visibility == View.VISIBLE) {
            UIVisibilityManager.setBarsTranslucent(mView.getAppCompatActivity(), false)
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

    override fun getActivity(): Activity = mView.getAppCompatActivity()

    override fun onGalleryHidden() {}

    override fun getGalleryActionBar(): GalleryActionBarUnit = mGalleryActionBarUnit
}
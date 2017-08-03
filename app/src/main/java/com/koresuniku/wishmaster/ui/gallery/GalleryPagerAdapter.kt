package com.koresuniku.wishmaster.ui.gallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.system.CacheUtils

class GalleryPagerAdapter(fragmentManager: FragmentManager, val viewPager: ViewPager,
                          files: List<Files>, var currentPosition: Int) :
        FragmentStatePagerAdapter(fragmentManager) {
    val LOG_TAG: String = GalleryPagerAdapter::class.java.simpleName

    var mFiles = files
    val mGalleryFragments: HashMap<Int, GalleryFragment> = HashMap()

    override fun getItem(position: Int): Fragment {
        mGalleryFragments.put(position, GalleryFragment(this, position))
        return mGalleryFragments[position]!!
    }

    override fun getCount(): Int {
        return mFiles.count()
    }

    fun onPageChanged(newPosition: Int) {
        //TODO: Pause the video
        if (mGalleryFragments[currentPosition]!!.mGalleryVideoUnit != null) {
            mGalleryFragments[currentPosition]!!.mGalleryVideoUnit!!.pauseVideoView()
        }

        //TODO: Video is paused
        currentPosition = newPosition

        //TODO: Start video view if needed
        if (mGalleryFragments[currentPosition]!!.mGalleryVideoUnit != null) {
            mGalleryFragments[currentPosition]!!.mGalleryVideoUnit!!.startVideoView()
        }

    }

    override fun destroyItem(container: View?, position: Int, `object`: Any?) {
        super.destroyItem(container, position, `object`)
        mGalleryFragments[position]!!.onDestroyItem()
        mGalleryFragments.remove(position)
    }

    fun onBackPressed() {
        mGalleryFragments.forEach { it.component2().onDestroyItem() }
        mGalleryFragments.clear()
        CacheUtils.trimCache(viewPager.context)
    }
}
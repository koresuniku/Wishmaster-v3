package com.koresuniku.wishmaster.ui.gallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.koresuniku.wishmaster.http.thread_list_api.model.Files

class GalleryPagerAdapter(fragmentManager: FragmentManager, files: List<Files>, var currentPosition: Int) :
        FragmentStatePagerAdapter(fragmentManager) {
    val LOG_TAG: String = GalleryPagerAdapter::class.java.simpleName

    var mFiles = files
    val mGalleryFragments: HashMap<Int, GalleryFragment> = HashMap()

    override fun getItem(position: Int): Fragment {
        mGalleryFragments.put(position, GalleryFragment())
        return mGalleryFragments[position]!!
    }

    override fun getCount(): Int {
        return mFiles.count()
    }

    fun onPageChanged(newPosition: Int) {
        //TODO: Pause the video

        //TODO: Video is paused
        currentPosition = newPosition

        //TODO: Start video view if needed
    }
}
package com.koresuniku.wishmaster.ui.gallery

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.system.App
import com.koresuniku.wishmaster.system.CacheUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager

class GalleryPagerAdapter(fragmentManager: FragmentManager, val galleryPagerView: GalleryPagerView,
                          files: List<Files>, var currentPosition: Int) :
        FragmentStatePagerAdapter(fragmentManager), GalleryPagerAdapterView {
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

    override fun getGalleryActionBar(): GalleryActionBarUnit {
        return galleryPagerView.getGalleryActionBar()
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
            mGalleryFragments[currentPosition]!!.mGalleryVideoUnit!!.onSoundChanged(App.soundVolume)
            mGalleryFragments[currentPosition]!!.mGalleryVideoUnit!!.startVideoView()
        }

    }

    override fun onUiVisibilityChanged(isShown: Boolean, position: Int) {
        mGalleryFragments.filterNot { it.key == position }.forEach { it.component2().onUiVisibilityChanged(isShown, false) }
//        for (fragment in mGalleryFragments) {
//            if (fragment.key != position) {
//                fragment.value.on
//            }
//        }
    }

    override fun getAdapter(): GalleryPagerAdapter {
        return this
    }

    fun getActivity(): Activity {
        return galleryPagerView.getActivity()
    }

    override fun destroyItem(container: View?, position: Int, `object`: Any?) {
        super.destroyItem(container, position, `object`)
        mGalleryFragments[position]!!.onDestroyItem()
        mGalleryFragments.remove(position)
    }

    fun onBackPressed() {
        UIVisibilityManager.showSystemUI(galleryPagerView.getActivity())
        mGalleryFragments.forEach { it.component2().onDestroyItem() }
        mGalleryFragments.clear()
        CacheUtils.trimCache(galleryPagerView.getViewPager().context)
    }
}
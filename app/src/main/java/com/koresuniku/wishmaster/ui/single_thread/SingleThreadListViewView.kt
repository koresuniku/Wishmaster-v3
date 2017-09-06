package com.koresuniku.wishmaster.ui.single_thread

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ListView
import com.koresuniku.wishmaster.domain.BaseJsonSchemaImpl
import com.koresuniku.wishmaster.ui.controller.SwipyRefreshLayoutUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.FilesAdapterView

interface SingleThreadListViewView : FilesAdapterView {

    fun getSchema(): BaseJsonSchemaImpl

    fun getSingleThreadListView(): ListView

    fun getBoardId(): String?

    fun showPostDialog(position: Int)

    fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit

    fun getAppCompatActivity(): AppCompatActivity

    fun getGalleryLayoutContainer(): ViewGroup

    fun getViewPager(): ViewPager
}
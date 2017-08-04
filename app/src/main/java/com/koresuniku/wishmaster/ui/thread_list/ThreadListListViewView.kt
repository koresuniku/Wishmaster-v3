package com.koresuniku.wishmaster.ui.thread_list

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ListView
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.controller.SwipyRefreshLayoutUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.FilesAdapterView

interface ThreadListListViewView : FilesAdapterView {

    fun getSchema(): ThreadListJsonSchema

    fun getThreadListView(): ListView

    fun getBoardId(): String?

    fun showPostDialog(position: Int)

    fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit

    fun openThread(threadName: String)

    fun getAppCompatActivity(): AppCompatActivity

    fun getGalleryLayoutContainer(): ViewGroup

    fun getViewPager(): ViewPager

    fun notifyGalleryShown()

    fun notifyGalleryHidden()
}
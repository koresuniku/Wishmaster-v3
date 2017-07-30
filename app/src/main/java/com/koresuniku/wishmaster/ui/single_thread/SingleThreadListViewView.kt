package com.koresuniku.wishmaster.ui.single_thread

import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ListView
import com.koresuniku.wishmaster.http.IBaseJsonSchemaImpl
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import com.koresuniku.wishmaster.ui.controller.SwipyRefreshLayoutUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.FilesAdapterView
import com.koresuniku.wishmaster.ui.controller.view_interface.IListViewAdapterCreatable

interface SingleThreadListViewView : FilesAdapterView {

    fun getSchema(): IBaseJsonSchemaImpl

    fun getSingleThreadListView(): ListView

    fun getBoardId(): String?

    fun showPostDialog(position: Int)

    fun getSwipyRefreshLayoutUnit(): SwipyRefreshLayoutUnit

    fun getAppCompatActivity(): AppCompatActivity

    fun getGalleryLayoutContainer(): ViewGroup

}
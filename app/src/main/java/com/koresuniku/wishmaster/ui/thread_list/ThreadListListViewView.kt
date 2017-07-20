package com.koresuniku.wishmaster.ui.thread_list

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
}
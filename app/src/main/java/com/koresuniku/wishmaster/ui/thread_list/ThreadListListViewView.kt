package com.koresuniku.wishmaster.ui.thread_list

import android.app.Activity
import android.widget.ListView
import com.koresuniku.wishmaster.http.thread_list_api.model.ThreadListJsonSchema
import java.text.ParsePosition

interface ThreadListListViewView {
    fun getActivity(): Activity

    fun getSchema(): ThreadListJsonSchema

    fun getThreadListView(): ListView

    fun getBoardId(): String?

    fun showPostDialog(position: Int)
}
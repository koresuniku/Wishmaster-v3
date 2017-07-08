package com.koresuniku.wishmaster.ui.thread_list

import android.util.Log
import android.view.View

class FullPostUnit(val mView: FullPostView) {
    val LOG_TAG: String = FullPostUnit::class.java.simpleName

    fun openFullPost(view: View) {
        Log.d(LOG_TAG, "opening full post, view is null " + (view == null))
    }
}
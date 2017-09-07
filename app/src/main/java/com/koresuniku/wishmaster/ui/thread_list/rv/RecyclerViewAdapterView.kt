package com.koresuniku.wishmaster.ui.thread_list.rv

import com.koresuniku.wishmaster.ui.controller.view_interface.IActivityView

interface RecyclerViewAdapterView : IActivityView {
    fun showProgressBar()
    fun hideProgressBar()
    fun attachAdapter()
}
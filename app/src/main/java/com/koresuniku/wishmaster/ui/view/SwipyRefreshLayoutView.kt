package com.koresuniku.wishmaster.ui.view

import android.widget.ListView
import com.koresuniku.wishmaster.ui.controller.AppBarLayoutUnit
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout

interface SwipyRefreshLayoutView : IActivityView {

    fun getRefreshLayout(): SwipyRefreshLayout

    fun getAppBarLayoutUnit(): AppBarLayoutUnit

    fun getListView(): ListView

    fun getListViewAdapter(): INotifyableListViewAdapter

    fun loadData()

}
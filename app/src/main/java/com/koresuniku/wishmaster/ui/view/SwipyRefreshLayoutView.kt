package com.koresuniku.wishmaster.ui.view

import android.support.design.widget.AppBarLayout
<<<<<<< HEAD
import android.support.v7.widget.RecyclerView
=======
>>>>>>> origin/master
import android.widget.ListView
import com.koresuniku.wishmaster.ui.controller.AppBarLayoutUnit
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout

<<<<<<< HEAD
interface SwipyRefreshLayoutView : IActivityView, LoadDataView {
=======
interface SwipyRefreshLayoutView : IActivityView {
>>>>>>> origin/master
    fun getRefreshLayout(): SwipyRefreshLayout

    fun getAppBarLayoutUnit(): AppBarLayoutUnit

    fun getListView(): ListView
<<<<<<< HEAD

    fun getListViewAdapter(): INotifyableLisViewAdapter

=======
>>>>>>> origin/master
}
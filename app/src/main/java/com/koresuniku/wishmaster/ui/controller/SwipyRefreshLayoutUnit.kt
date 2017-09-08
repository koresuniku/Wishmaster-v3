package com.koresuniku.wishmaster.ui.controller

import android.util.Log
import com.koresuniku.wishmaster.ui.controller.view_interface.SwipyRefreshLayoutView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection

class SwipyRefreshLayoutUnit(val mView: SwipyRefreshLayoutView) {
    val LOG_TAG: String = SwipyRefreshLayoutUnit::class.java.simpleName

    var mRefreshLayout: SwipyRefreshLayout? = null

    init {
        onCreate()
    }

    fun onCreate() {
        mRefreshLayout = mView.getRefreshLayout()
        mRefreshLayout!!.direction = SwipyRefreshLayoutDirection.BOTH
        mRefreshLayout!!.setDistanceToTriggerSync(75)
        mRefreshLayout!!.isEnabled = true

        mRefreshLayout!!.setOnRefreshListener { mView.loadData() }

        enableTop()
    }

    fun disableRefreshLayout() {
        mRefreshLayout!!.isEnabled = false
    }

    fun enableTop() {
        mRefreshLayout!!.direction = SwipyRefreshLayoutDirection.TOP
        mRefreshLayout!!.isEnabled = true
    }

    fun enableBottom() {
        mRefreshLayout!!.direction = SwipyRefreshLayoutDirection.BOTTOM
        mRefreshLayout!!.isEnabled = true
    }

    fun setRefreshing(refreshing: Boolean) {
        mRefreshLayout!!.isRefreshing = refreshing
    }

    fun isRefreshing(): Boolean {
        return mRefreshLayout!!.isRefreshing
    }

    fun checkRefreshAvailability() {
        val readyToRefreshTop: Boolean = !mView.getListView().canScrollVertically(-1)
                && mView.getAppBarLayoutUnit().appBarVerticalOffset == 0
        val readyToRefreshBottom: Boolean = !mView.getListView().canScrollVertically(1)
                && Math.abs(mView.getAppBarLayoutUnit().appBarVerticalOffset) ==

                mView.getAppBarLayoutUnit().mAppBarLayout.totalScrollRange

                mView.getAppBarLayoutUnit().appBarLayoutExpandedValue


        if (readyToRefreshTop) {
            this.enableTop()
            //Log.d(LOG_TAG, "readyToRefreshTop")
        }
        if (readyToRefreshBottom){
            this.enableBottom()
           // Log.d(LOG_TAG, "readyToRefreshBottom")
        }

        if (!readyToRefreshTop && !readyToRefreshBottom) this.disableRefreshLayout()
    }

    fun onDataLoaded(newPostsCame: Boolean) {
        Log.d(LOG_TAG, "onDataLoaded:")
        mRefreshLayout!!.isRefreshing = false
        disableRefreshLayout()
        if (mRefreshLayout!!.direction == SwipyRefreshLayoutDirection.TOP) {
            mView.getListView().post { mView.getListView().setSelectionAfterHeaderView() }
            mView.getAppBarLayoutUnit().mAppBarLayout.setExpanded(true)
            if (newPostsCame) {
                Log.d(LOG_TAG, "notifying adapter:")
                mView.getListViewAdapter().iNotifyDataSetChanged()
            }
        } else {
            if (newPostsCame) {
//                val preCount = mView.getListView().count
//                mView.getListViewAdapter().iNotifyDataSetChanged()
//                val afterCount = mView.getListView().count
//                Log.d(LOG_TAG, "pre: $preCount, after: $afterCount")
//                if (preCount != afterCount) {
//                    mView.getListView().post {
//                        if (afterCount - preCount < 10)
//                            mView.getListView().smoothScrollToPosition(preCount + 1)
//                        else mView.getListView().setSelection(preCount + 1)
//                    }
//                }
            }
        }
    }

    fun onDataLoadedReturnToTop() {
        Log.d(LOG_TAG, "onDataLoaded:")
        mRefreshLayout!!.isRefreshing = false
        disableRefreshLayout()
        mView.getListView().post { mView.getListView().setSelectionAfterHeaderView() }
        mView.getAppBarLayoutUnit().mAppBarLayout.setExpanded(true)
        mView.getListViewAdapter().iNotifyDataSetChanged()
    }

}
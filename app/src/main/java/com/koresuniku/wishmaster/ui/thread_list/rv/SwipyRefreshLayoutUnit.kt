package com.koresuniku.wishmaster.ui.thread_list.rv

import android.app.Activity
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.widget.FixedRecyclerView
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection

class SwipyRefreshLayoutUnit(private val mActivity: Activity,
                             private val mRecyclerView: FixedRecyclerView,
                             private val mCallback: RefreshLayoutCallback) {

    interface RefreshLayoutCallback {
        fun doRefresh()
    }


    val LOG_TAG: String = SwipyRefreshLayoutUnit::class.java.simpleName

    private lateinit var mRefreshLayout: SwipyRefreshLayout
    private lateinit var mAppBarLayoutUnit: AppBarLayoutUnit

    init { onCreate() }

    fun onCreate() {
        mAppBarLayoutUnit = AppBarLayoutUnit()

        mRefreshLayout = mActivity.findViewById(R.id.srl) as SwipyRefreshLayout
        mRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTH
        mRefreshLayout.setDistanceToTriggerSync(75)
        mRefreshLayout.isEnabled = true
        mRefreshLayout.setOnRefreshListener { mCallback.doRefresh() }

        enableTop()
    }

    private fun disableRefreshLayout() { mRefreshLayout.isEnabled = false }

    private fun enableTop() {
        mRefreshLayout.direction = SwipyRefreshLayoutDirection.TOP
        mRefreshLayout.isEnabled = true
    }

    private fun enableBottom() {
        mRefreshLayout.direction = SwipyRefreshLayoutDirection.BOTTOM
        mRefreshLayout.isEnabled = true
    }

    fun setRefreshing(refreshing: Boolean) { mRefreshLayout.isRefreshing = refreshing }

    fun isRefreshing(): Boolean = mRefreshLayout.isRefreshing

    fun checkAbleToRefresh() {
        val readyToRefreshTop: Boolean = !mRecyclerView.canScrollVertically(-1)
                && mAppBarLayoutUnit.appBarVerticalOffset == 0
        val readyToRefreshBottom: Boolean = !mRecyclerView.canScrollVertically(1)
                && Math.abs(mAppBarLayoutUnit.appBarVerticalOffset) ==
                mAppBarLayoutUnit.mAppBarLayout.totalScrollRange

        if (readyToRefreshTop) { this.enableTop() }

        if (readyToRefreshBottom) { this.enableBottom() }

        if (!readyToRefreshTop && !readyToRefreshBottom) this.disableRefreshLayout()
    }

    fun expandAppBarLayout() { mAppBarLayoutUnit.mAppBarLayout.setExpanded(true, true) }

//    fun onDataLoaded(newPostsCame: Boolean) {
//        Log.d(LOG_TAG, "onDataLoaded:")
//        mRefreshLayout!!.isRefreshing = false
//        disableRefreshLayout()
//        if (mRefreshLayout!!.direction == SwipyRefreshLayoutDirection.TOP) {
//            mView.getListView().post { mView.getListView().setSelectionAfterHeaderView() }
//            mView.getAppBarLayoutUnit().mAppBarLayout.setExpanded(true)
//            if (newPostsCame) {
//                Log.d(LOG_TAG, "notifying adapter:")
//                mView.getListViewAdapter().iNotifyDataSetChanged()
//            }
//        }
//    }

//    fun onDataLoadedReturnToTop() {
//        Log.d(LOG_TAG, "onDataLoaded:")
//        mRefreshLayout!!.isRefreshing = false
//        disableRefreshLayout()
//        mView.getListView().post { mView.getListView().setSelectionAfterHeaderView() }
//        mView.getAppBarLayoutUnit().mAppBarLayout.setExpanded(true)
//        mView.getListViewAdapter().iNotifyDataSetChanged()
//    }

    inner class AppBarLayoutUnit {
        val LOG_TAG: String = AppBarLayoutUnit::class.java.simpleName

        val mAppBarLayout: AppBarLayout = mActivity.findViewById(R.id.app_bar_layout) as AppBarLayout
        var appBarVerticalOffset: Int = 0
        var appBarLayoutExpandedValue: Int = mAppBarLayout.totalScrollRange

        init { onCreate() }

        fun onCreate() {
            mAppBarLayout.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->
                appBarVerticalOffset = verticalOffset })
        }
    }

}
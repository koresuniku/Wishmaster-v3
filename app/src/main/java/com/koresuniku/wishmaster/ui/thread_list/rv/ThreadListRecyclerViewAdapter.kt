package com.koresuniku.wishmaster.ui.thread_list.rv

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.IntentUtils
import com.koresuniku.wishmaster.ui.controller.ClickableAdapter
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadActivity
import com.koresuniku.wishmaster.ui.thread_list.ThreadListActivity
import com.koresuniku.wishmaster.ui.widget.FixedRecyclerView
import android.support.v4.content.ContextCompat
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.widget.AbsListView
import com.bumptech.glide.Glide
import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration
import com.koresuniku.wishmaster.ui.controller.ProgressUnit
import com.koresuniku.wishmaster.ui.dialog.DialogManager
import com.koresuniku.wishmaster.ui.gallery.GalleryUnit
import com.koresuniku.wishmaster.ui.widget.recycler_view_fast_scroll.RecyclerFastScroller
import org.jetbrains.anko.find

class ThreadListRecyclerViewAdapter(private val mActivity: ThreadListActivity, val boardId: String) :
        RecyclerView.Adapter<ThreadListRecyclerViewViewHolder>(), RecyclerViewAdapterView,
        ClickableAdapter, SwipyRefreshLayoutUnit.RefreshLayoutCallback {
    val LOG_TAG: String = ThreadListRecyclerViewAdapter::class.java.simpleName

    private val mThreadListPresenter = ThreadListPresenter(this, boardId)
    internal val mGalleryUnit = GalleryUnit(mActivity)

    private var mRecyclerView: FixedRecyclerView = mActivity.findViewById(R.id.rv) as FixedRecyclerView
    private var mFastScroller: RecyclerFastScroller = mActivity.findViewById(R.id.fs) as RecyclerFastScroller
    private var mScrollState: Int = AbsListView.OnScrollListener.SCROLL_STATE_IDLE

    private var mSwipyRefreshLayoutUnit = SwipyRefreshLayoutUnit(mActivity, mRecyclerView, this)
    private var mProgressUnit = ProgressUnit(mActivity, mActivity.find(R.id.progress_container))

    fun initAdapter() { mThreadListPresenter.loadData(boardId) }

    override fun onClickNoSpoilersOrLinksFound(threadNumber: String) { openThread(threadNumber) }

    override fun onLongClickNoSpoilersOrLinksFound(threadNumber: String) {}

    override fun onLongClickLinkFound(link: String) {}

    override fun doRefresh() {
        if (!mSwipyRefreshLayoutUnit.isRefreshing()) mSwipyRefreshLayoutUnit.setRefreshing(true)
        mThreadListPresenter.loadData(boardId)
    }

    override fun showProgressBar() {
        Log.d(LOG_TAG, "showProgressBar")
        mProgressUnit.showProgressYoba()
    }

    override fun hideProgressBar() {
        Log.d(LOG_TAG, "hideProgressBar")
        mProgressUnit.hideProgressYoba()
    }

    override fun onDataLoadingFailed() {

    }

    override fun onDataLoadedSuccessfully() {
        if (mRecyclerView.adapter == null) attachAdapter()
        else {
            mSwipyRefreshLayoutUnit.setRefreshing(false)
            mSwipyRefreshLayoutUnit.checkAbleToRefresh()
            mSwipyRefreshLayoutUnit.expandAppBarLayout()
            (mRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            notifyDataSetChanged()
        }
    }

    private fun attachAdapter() {
        mRecyclerView.layoutManager = LinearLayoutManager(mActivity)
        mRecyclerView.adapter = this
        mRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mScrollState = newState
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> Glide.with (mActivity).pauseRequests()
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> Glide.with(mActivity).resumeRequests()
                }
            }
        })
        mRecyclerView.setOnTouchListener { v, event ->
            mSwipyRefreshLayoutUnit.checkAbleToRefresh(); false }

        mFastScroller.attachRecyclerView(mRecyclerView)
        mFastScroller.attachAdapter(mRecyclerView.adapter)
        mFastScroller.attachAppBarLayout(
                mActivity.findViewById(R.id.coordinator) as CoordinatorLayout,
                mActivity.findViewById(R.id.app_bar_layout) as AppBarLayout)

        val dividerDrawable =
                ContextCompat.getDrawable(mActivity, R.drawable.thread_list_recycler_view_divider)
        mRecyclerView.addItemDecoration(DividerItemDecoration(dividerDrawable))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThreadListRecyclerViewViewHolder {
        val itemView = LayoutInflater.from(mActivity)
                .inflate(R.layout.thread_item_multiple_images, parent, false)
        return ThreadListRecyclerViewViewHolder(mActivity, this, itemView)
    }

    override fun onBindViewHolder(holder: ThreadListRecyclerViewViewHolder?, position: Int) {
        if (holder != null) mThreadListPresenter.bindViewHolder(holder, position)
    }

    override fun getItemCount(): Int = mThreadListPresenter.getData().getItemList().size

    fun allowBackPress(): Boolean = mGalleryUnit.onBackPressed()

    fun openThread(threadNumber: String) {
        val intent = Intent(mActivity, SingleThreadActivity::class.java)

        intent.putExtra(IntentUtils.BOARD_ID_CODE, boardId)
        intent.putExtra(IntentUtils.BOARD_NAME_CODE, mThreadListPresenter.getData().getBoardModel().getBoardName())
        intent.putExtra(IntentUtils.THREAD_NUMBER_CODE, threadNumber)

        if (DeviceUtils.sdkIsLollipopOrHigher()) {
            mActivity.startActivity(intent)
            //this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        } else mActivity.startActivity(intent)
    }

    fun showPostDialog(position: Int) {
        val args = Bundle()
        args.putInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY, position)
        mActivity.removeDialog(0)
        mActivity.showDialog(0, args)
    }

    override fun getActivity(): Activity = mActivity
}
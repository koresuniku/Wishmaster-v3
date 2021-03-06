package com.koresuniku.wishmaster.ui.thread_list

import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.view_interface.IListViewAdapterCreatable
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

class ThreadListListViewUnit(val mView: ThreadListListViewView) : IListViewAdapterCreatable {
    val LOG_TAG: String = ThreadListListViewUnit::class.java.simpleName

    var mListView: ListView? = null
    var mListViewAdapter: ThreadListListViewAdapter? = null

    var adapterIsCreated: Boolean? = null

    init {
        adapterIsCreated = false
        mListView = mView.getThreadListView()
        mListView!!.setOnTouchListener { v, event ->
            mView.getSwipyRefreshLayoutUnit().checkRefreshAvailability(); false
        }
        //mListView!!.addHeaderView(createHeaderView())
        //mListView!!.addFooterView(createHeaderView())
    }

    override fun adapterIsCreated(): Boolean {
        return adapterIsCreated!!
    }

    fun notifyItemCommentTextViewChanged() {
        if (mListViewAdapter != null) mListViewAdapter!!.notifyItemCommentTextViewChanged()
    }

    fun notifyItemImageSizeChanged() {
        if (mListViewAdapter != null) mListViewAdapter!!.notifyItemImageSizeChanged()
    }

    override fun createListViewAdapter() {
        adapterIsCreated = true
        mListViewAdapter = ThreadListListViewAdapter(mView)
        mListView!!.adapter = mListViewAdapter
        mListView!!.onItemClickListener = null
        mListView!!.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Glide.with(mView.getActivity()).pauseRequests()
                } else {
                    Glide.with(mView.getActivity()).resumeRequests()
                }

            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {

            }
        })
    }

    fun createHeaderView(): View {
        return LayoutInflater.from(
                mView.getActivity().applicationContext)
                .inflate(R.layout.list_header, null, false)
    }
}
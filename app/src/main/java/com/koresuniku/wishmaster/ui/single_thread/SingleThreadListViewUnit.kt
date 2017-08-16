package com.koresuniku.wishmaster.ui.single_thread

import android.widget.AbsListView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.ui.controller.view_interface.IListViewAdapterCreatable
import com.koresuniku.wishmaster.ui.thread_list.ThreadListListViewAdapter

class SingleThreadListViewUnit(val mView: SingleThreadListViewView) : IListViewAdapterCreatable {
    val LOG_TAG: String = SingleThreadListViewUnit::class.java.simpleName

    var mListView: ListView? = null
    var mListViewAdapter: SingleThreadListViewAdapter? = null

    var adapterIsCreated: Boolean? = null

    init {
        adapterIsCreated = false
        mListView = mView.getSingleThreadListView()
        mListView!!.setOnTouchListener { v, event ->
            mView.getSwipyRefreshLayoutUnit().checkRefreshAvailability(); false
        }
    }

    override fun adapterIsCreated(): Boolean {
        return adapterIsCreated!!
    }

    override fun createListViewAdapter() {
        adapterIsCreated = true
        mListViewAdapter = SingleThreadListViewAdapter(mView)
        mListView!!.adapter = mListViewAdapter
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
}
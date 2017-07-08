package com.koresuniku.wishmaster.ui.thread_list

import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import com.bumptech.glide.Glide
import com.koresuniku.wishmaster.R
import org.jetbrains.anko.sdk25.coroutines.onScrollListener

class ThreadListListViewUnit(val mView: ThreadListListViewView) {
    val LOG_TAG: String = ThreadListListViewUnit::class.java.simpleName

    var mListView: ListView? = null
    var mListViewAdapter: ThreadListListViewAdapter? = null

    init {
        mListView = mView.getThreadListView()
        //mListView!!.addHeaderView(createHeaderView())
        //mListView!!.addFooterView(createHeaderView())
    }

    fun createListViewAdapter() {
        mListViewAdapter = ThreadListListViewAdapter(mView)
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

    fun createHeaderView(): View {
        return LayoutInflater.from(
                mView.getActivity().applicationContext)
                .inflate(R.layout.list_header, null, false)
    }
}
package com.koresuniku.wishmaster.ui.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.ProgressUnit
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find


class BoardListFragment(val mView: ExpandableListViewView?) : Fragment() {
    val LOG_TAG: String = BoardListFragment::class.java.simpleName

    lateinit var mExpandableListView: ExpandableListView
    lateinit var mExpandableListViewAdapter: BoardsExpandableListViewAdapter
    var mRootView: View = mView?.getActivity()!!.layoutInflater!!.inflate(R.layout.board_list_fragment, null,  false)
    var mProgressUnit: ProgressUnit = ProgressUnit(mView?.getActivity()!!, mRootView.find(R.id.progress_container))

    constructor(): this(null)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            mRootView

    fun onDataLoaded() {
        mRootView.find<View>(R.id.progress_container_container).visibility = View.GONE
        hideProgressYoba()
        setupExpandableListView()
    }

    fun setupExpandableListView() {
        mExpandableListView = mRootView.findViewById(R.id.board_list) as ExpandableListView
        mExpandableListView.visibility = View.VISIBLE
        mExpandableListViewAdapter = BoardsExpandableListViewAdapter(mView)
        mExpandableListView.setGroupIndicator(null)
        mExpandableListView.setAdapter(mExpandableListViewAdapter)
    }

    fun showProgressYoba() { mProgressUnit.showProgressYoba() }

    fun hideProgressYoba() { mProgressUnit.hideProgressYoba() }
}
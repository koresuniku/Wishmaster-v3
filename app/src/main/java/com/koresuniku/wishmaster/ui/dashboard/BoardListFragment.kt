package com.koresuniku.wishmaster.ui.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.koresuniku.wishmaster.R

class BoardListFragment(var mView: ExpandableListViewView) : Fragment() {
    var mExpandableListView: ExpandableListView? = null
    var mExpandableListViewAdapter: BoardsExpandableListViewAdapter? = null
    val mRootView: View = mView.getActivity().layoutInflater.inflate(R.layout.board_list_fragment, null,  false)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return mRootView
    }

    fun onDataLoaded() {
        setupExpandableListView()
    }

    fun setupExpandableListView() {
        mExpandableListView = mRootView.findViewById(R.id.board_list) as ExpandableListView
        mExpandableListViewAdapter = BoardsExpandableListViewAdapter(mView)
        mExpandableListView!!.setGroupIndicator(null)
        mExpandableListView!!.setAdapter(mExpandableListViewAdapter)
    }
}
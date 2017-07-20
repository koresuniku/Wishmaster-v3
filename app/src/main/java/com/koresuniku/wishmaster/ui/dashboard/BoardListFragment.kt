package com.koresuniku.wishmaster.ui.dashboard

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.ProgressUnit
import com.koresuniku.wishmaster.ui.controller.view_interface.ProgressView
import org.jetbrains.anko.find


class BoardListFragment(var mView: ExpandableListViewView) : Fragment(), ProgressView {
    val LOG_TAG: String = BoardListFragment::class.java.simpleName

    var mExpandableListView: ExpandableListView? = null
    var mExpandableListViewAdapter: BoardsExpandableListViewAdapter? = null
    val mRootView: View = mView.getActivity().layoutInflater.inflate(R.layout.board_list_fragment, null,  false)

    var mProgressUnit: ProgressUnit = ProgressUnit(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return mRootView
    }

    fun onDataLoaded() {
        mRootView.find<View>(R.id.progress_container_container).visibility = View.GONE
        hideProgressYoba()
        setupExpandableListView()
    }



    fun setupExpandableListView() {
        mExpandableListView = mRootView.findViewById(R.id.board_list) as ExpandableListView
        mExpandableListView!!.visibility = View.VISIBLE
        mExpandableListViewAdapter = BoardsExpandableListViewAdapter(mView)
        mExpandableListView!!.setGroupIndicator(null)
        mExpandableListView!!.setAdapter(mExpandableListViewAdapter)
    }

    fun showProgressYoba() {

        mProgressUnit.showProgressYoba()
    }

    fun hideProgressYoba() {
        mProgressUnit.hideProgressYoba()
    }

    override fun getProgressContainer(): View {
        return mRootView.find(R.id.progress_container)
    }

    override fun getActivityOverridden(): Activity {
        return mView.getActivity()
    }
}
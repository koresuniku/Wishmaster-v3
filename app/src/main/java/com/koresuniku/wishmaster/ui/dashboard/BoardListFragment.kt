package com.koresuniku.wishmaster.ui.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ExpandableListView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.R.id.imageView
import android.support.v4.view.ViewCompat.animate
import com.koresuniku.wishmaster.R.id.imageView
import android.support.v4.view.ViewCompat.animate
import android.util.Log


class BoardListFragment(var mView: ExpandableListViewView) : Fragment() {
    val LOG_TAG: String = BoardListFragment::class.java.simpleName

    var mExpandableListView: ExpandableListView? = null
    var mExpandableListViewAdapter: BoardsExpandableListViewAdapter? = null
    val mRootView: View = mView.getActivity().layoutInflater.inflate(R.layout.board_list_fragment, null,  false)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return mRootView
    }

    fun onDataLoaded() {
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
        mRootView.findViewById(R.id.progress_yoba_container).post {
            mRootView.findViewById(R.id.progress_yoba_container).visibility = View.VISIBLE
            val runnable = object : Runnable {
                override fun run() {
                    mRootView.findViewById(R.id.loading_yoba).animate().rotationBy(360f)
                            .withEndAction(this).setDuration(1000).setInterpolator(LinearInterpolator()).start()
                }
            }
            mRootView.findViewById(R.id.loading_yoba).animate().rotationBy(360f)
                    .withEndAction(runnable).setDuration(1000).setInterpolator(LinearInterpolator()).start()
        }
    }

    fun hideProgressYoba() {
        mRootView.findViewById(R.id.progress_yoba_container).post {
            mRootView.findViewById(R.id.progress_yoba_container).visibility = View.GONE
        }
        mRootView.findViewById(R.id.progress_yoba_container).findViewById(R.id.loading_yoba).clearAnimation()
    }
}
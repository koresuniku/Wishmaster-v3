package com.koresuniku.wishmaster.ui.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koresuniku.wishmaster.R

class HistoryFragment : Fragment() {
    var mRootView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater!!.inflate(R.layout.history_fragment, container, false)

        return mRootView
    }
}
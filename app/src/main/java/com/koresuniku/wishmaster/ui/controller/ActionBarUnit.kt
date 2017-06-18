package com.koresuniku.wishmaster.ui.controller

import android.content.res.Configuration
import android.support.v7.widget.Toolbar
import android.widget.FrameLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.view.ActionBarView

class ActionBarUnit(val mView: ActionBarView) {
    val mActivityToolbarContainer: FrameLayout = mView.getToolbarContainer()
    var mLocalToolbarContainer: FrameLayout? = null
    var mToolbar: Toolbar? = null

    init {
        setupActionBar(mView.getAppCompatActivity().resources.configuration)
    }

    fun setupActionBar(configuration: Configuration) {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mView.getAppCompatActivity().layoutInflater.inflate(R.layout.action_bar_layout, null, false) as FrameLayout
        mToolbar = mLocalToolbarContainer!!.findViewById(R.id.toolbar) as Toolbar?
        val height: Int
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar!!.layoutParams.height = height
        mToolbar!!.layoutParams.width = Toolbar.LayoutParams.MATCH_PARENT

        mActivityToolbarContainer.addView(mLocalToolbarContainer)
    }

    fun onConfigurationChanged(configuration: Configuration) {
        setupActionBar(configuration)
    }
}
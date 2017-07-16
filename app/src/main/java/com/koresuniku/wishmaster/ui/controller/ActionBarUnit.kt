package com.koresuniku.wishmaster.ui.controller

import android.content.res.Configuration
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.system.App
import com.koresuniku.wishmaster.system.DeviceUtils
import com.koresuniku.wishmaster.ui.view.ActionBarView

class ActionBarUnit(val mView: ActionBarView, val createTopMargin: Boolean) {
    val LOG_TAG: String = ActionBarWithTabsUnit::class.java.simpleName

    val mActivityToolbarContainer: FrameLayout = mView.getToolbarContainer()

    var mLocalToolbarContainer: RelativeLayout? = null
    var mToolbar: Toolbar? = null

    init {
        setupActionBar(mView.getAppCompatActivity().resources.configuration)
    }

    fun setupActionBar(configuration: Configuration) {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mView.getAppCompatActivity().layoutInflater
                .inflate(R.layout.action_bar_layout, null, false) as RelativeLayout
        mToolbar = mLocalToolbarContainer!!.findViewById(R.id.toolbar) as Toolbar?
        val height: Int
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            height = mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar!!.layoutParams!!.height = height
        mToolbar!!.layoutParams!!.width = Toolbar.LayoutParams.MATCH_PARENT
        if (createTopMargin) setProperDimensForToolbarContainer()

        mActivityToolbarContainer.addView(mLocalToolbarContainer)

        mView.getAppCompatActivity().setSupportActionBar(mToolbar)


        mView.setupActionBarTitle()
    }

    fun setProperDimensForToolbarContainer() {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            mLocalToolbarContainer!!.setPadding(0,
                    mView.getAppCompatActivity().resources.getDimension(R.dimen.status_bar_height).toInt(),
                    0, 0)
        }
    }


    fun showTabLayout() {
        mLocalToolbarContainer!!.findViewById(R.id.tab_layout).visibility = View.VISIBLE
    }


    fun onConfigurationChanged(configuration: Configuration) {
        setupActionBar(configuration)
    }

}
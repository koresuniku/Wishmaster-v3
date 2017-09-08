package com.koresuniku.wishmaster.ui.controller

import android.content.res.Configuration
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.controller.view_interface.ActionBarView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class ActionBarUnit(var mView: ActionBarView, val createTopMargin: Boolean, val presetupTitleLoading: Boolean) {
    open var LOG_TAG: String = ActionBarWithTabsUnit::class.java.simpleName

    val mActivityToolbarContainer: FrameLayout = mView.getToolbarContainer()

    var mLocalToolbarContainer: RelativeLayout? = null
    var mToolbar: Toolbar? = null

    init {
        EventBus.getDefault().register(this)
        setupActionBar(mView.getAppCompatActivity().resources.configuration)
        if (presetupTitleLoading) setLoadingTitle()
    }

    fun setupActionBar(configuration: Configuration) {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mView.getAppCompatActivity().layoutInflater
                .inflate(R.layout.action_bar_layout, null, false) as RelativeLayout
        mToolbar = mLocalToolbarContainer!!.findViewById(R.id.toolbar) as Toolbar?
        val height: Int = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            mView.getAppCompatActivity().resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar!!.layoutParams!!.height = height
        mToolbar!!.layoutParams!!.width = Toolbar.LayoutParams.MATCH_PARENT
        if (createTopMargin) setProperDimensForToolbarContainer()

        mActivityToolbarContainer.addView(mLocalToolbarContainer)

        setSupportActionBarAndTitle()
    }

    open fun setSupportActionBarAndTitle() {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecyclerEvent(event: LifecycleEvent) {
        when(event.anEvent) {
            LifecycleEvent.ON_CONFIGURATION_CHANGED -> {
                val configuration = event.configuration
                setupActionBar(configuration)
            }
            LifecycleEvent.ON_START -> EventBus.getDefault().register(this)

            LifecycleEvent.ON_STOP -> EventBus.getDefault().unregister(this)
        }
    }
    open fun onConfigurationChanged(configuration: Configuration) {

    }

    fun setLoadingTitle() {
        mView.getAppCompatActivity().supportActionBar!!.title =
                mView.getAppCompatActivity().getString(R.string.loading_text)
    }

}
package com.koresuniku.wishmaster.ui.action_bar

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.DeviceUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.configuration
import org.jetbrains.anko.find

class PostingActionBarController(private val mAppCompatActivity: AppCompatActivity,
                                 private val mSetTopMargin: Boolean) {
    val LOG_TAG: String  = PostingActionBarController::class.java.simpleName

    lateinit var mActivityToolbarContainer: ViewGroup
    lateinit var mLocalToolbarContainer: RelativeLayout
    lateinit var mToolbar: Toolbar

    init {
        mActivityToolbarContainer = mAppCompatActivity.find(R.id.toolbar_container)
        EventBus.getDefault().register(this)
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event.anEvent) {
            LifecycleEvent.onStart ->
                if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
            LifecycleEvent.onStop ->
                if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
            LifecycleEvent.onConfigurationChanged -> setupActionBar()
        }
    }

    fun setupActionBar() {
        mActivityToolbarContainer.removeAllViews()

        mLocalToolbarContainer = mAppCompatActivity.layoutInflater
                .inflate(R.layout.action_bar_layout, null, false) as RelativeLayout
        mToolbar = mLocalToolbarContainer.findViewById(R.id.toolbar) as Toolbar
        val height: Int = if (mAppCompatActivity.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mAppCompatActivity.resources.getDimension(R.dimen.action_bar_height_portrait).toInt()
        } else {
            mAppCompatActivity.resources.getDimension(R.dimen.action_bar_height_landscape).toInt()
        }
        mToolbar.layoutParams!!.height = height
        mToolbar.layoutParams!!.width = Toolbar.LayoutParams.MATCH_PARENT

        if (mSetTopMargin) setTopMargin()

        mActivityToolbarContainer.addView(mLocalToolbarContainer)
        mAppCompatActivity.setSupportActionBar(mToolbar)

        setupTitle()
    }

    private fun setTopMargin() {
        if (DeviceUtils.sdkIsKitkatOrHigher()) {
            mLocalToolbarContainer.setPadding(0,
                    mAppCompatActivity.resources.getDimension(R.dimen.status_bar_height).toInt(),
                    0, 0)
        }
    }

    private fun setupTitle() {
        mAppCompatActivity.supportActionBar!!.title = mAppCompatActivity.getString(R.string.new_post_text)
        mAppCompatActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mAppCompatActivity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

}
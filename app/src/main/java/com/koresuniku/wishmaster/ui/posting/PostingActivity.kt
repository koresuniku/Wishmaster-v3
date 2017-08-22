package com.koresuniku.wishmaster.ui.posting

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.action_bar.PostingActionBarController
import org.greenrobot.eventbus.EventBus

class PostingActivity : AppCompatActivity() {
    val LOG_TAG: String = PostingActivity::class.java.simpleName

    private lateinit var mPostingActionBarController: PostingActionBarController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        mPostingActionBarController = PostingActionBarController(this, false)
        mPostingActionBarController.setupActionBar()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.onStart))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.onConfigurationChanged))
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.onStop))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> { onBackPressed(); return true }
        }

        return super.onOptionsItemSelected(item)
    }
}
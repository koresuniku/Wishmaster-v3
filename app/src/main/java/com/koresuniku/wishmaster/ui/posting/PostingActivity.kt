package com.koresuniku.wishmaster.ui.posting

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.bindView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.IntentUtils
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.action_bar.PostingActionBarController
import com.koresuniku.wishmaster.ui.text.TextUtils
import org.greenrobot.eventbus.EventBus

class PostingActivity : AppCompatActivity() {
    val LOG_TAG: String = PostingActivity::class.java.simpleName

    lateinit private var mWhomAnswered: String

    private lateinit var mPostingActionBarController: PostingActionBarController

    private val mCommentEditText: EditText by bindView(R.id.comment_edit_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        ButterKnife.bind(this, this)

        mWhomAnswered = intent.getStringExtra(IntentUtils.WHOM_TO_ANSWER_CODE)

        mPostingActionBarController = PostingActionBarController(this)
        mPostingActionBarController.setupActionBar()

        setupCommentEditText()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.ON_START))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.ON_CONFIGURATION_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().post(LifecycleEvent(LifecycleEvent.ON_STOP))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> { onBackPressed(); return true }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupCommentEditText() {
        mCommentEditText.setText(
                TextUtils.getMakabaFormattedAnswerString("$mWhomAnswered\n"),
                TextView.BufferType.SPANNABLE)
        mCommentEditText.setSelection(mCommentEditText.text.lastIndex)

    }
}
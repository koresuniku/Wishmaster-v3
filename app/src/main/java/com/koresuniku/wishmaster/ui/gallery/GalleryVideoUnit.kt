package com.koresuniku.wishmaster.ui.gallery

import android.content.res.Configuration
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import com.devbrackets.android.exomedia.listener.OnCompletionListener
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.http.Dvach
import com.koresuniku.wishmaster.http.thread_list_api.model.Files
import com.koresuniku.wishmaster.system.App
import com.koresuniku.wishmaster.system.DeviceUtils
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.UIVisibilityManager
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.dimen
import java.util.concurrent.TimeUnit

class GalleryVideoUnit(val mFragment: GalleryFragment, val file: Files) :
        View.OnClickListener, OnPreparedListener, OnCompletionListener {
    val LOG_TAG: String = GalleryVideoUnit::class.java.simpleName

    val sHandler: Handler = Handler()

    var mVideoLayout: ViewGroup? = null
    var mProgressBar: ProgressBar? = null
    var mVideoView: VideoView? = null

    var mVideoControlsContainer: FrameLayout? = null
    var mVideoControlsLayout: FrameLayout? = null
    var mCurrentProgress: TextView? = null
    var mVideoProgress: SeekBar? = null
    var mOverallDuration: TextView? = null
    var mPlayPauseContainer: FrameLayout? = null
    var mPlayPauseImage: ImageView? = null
    var mSoundSwitcherContainer: FrameLayout? = null
    var mSoundSwitcherImage: ImageView? = null

    var animCollapseControls: ScaleAnimation? = null
    var animExpandControls: ScaleAnimation? = null

    var isPrepared: Boolean = false
    var isCompleted: Boolean = false
    var previousVolume: Int = 1
    var mDuration: Long = 0L

    init {
        onCreate()
        setupAnimations()
    }

    fun createVideoView() {
        mVideoView = VideoView(mFragment.context)
        mVideoView!!.setControls(null)
        mVideoLayout!!.addView(mVideoView)
    }

    fun onCreate() {
        mVideoLayout = mFragment.context.layoutInflater.inflate(
                R.layout.gallery_video_layout, FrameLayout(mFragment.context), false) as ViewGroup
        mProgressBar = mVideoLayout!!.find(R.id.progressBar)
        createVideoView()
        mVideoControlsContainer = mVideoLayout!!.find(R.id.video_controls_container)
        if (!UIVisibilityManager.isSystemUiShown) mVideoControlsContainer!!.visibility = View.GONE

        mVideoView!!.setOnPreparedListener(this)
        mVideoView!!.setOnClickListener(this)
        mVideoView!!.setVideoPath(Dvach.DVACH_BASE_URL + file.getPath())

        setupControlView()

        mVideoView!!.setOnBufferUpdateListener { percent -> run {
            mVideoProgress!!.secondaryProgress = percent }
        }
        mVideoView!!.setOnCompletionListener(this)
        onSoundChanged(App.soundVolume)

        mFragment.mRootView!!.addView(mVideoLayout)
    }

    fun setupAnimations() {
        animExpandControls = ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f)
        animExpandControls!!.duration = 250
        animCollapseControls = ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f)
        animCollapseControls!!.duration = 250

        animExpandControls!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                mVideoControlsContainer!!.visibility = View.VISIBLE
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        animCollapseControls!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {

            }

            override fun onAnimationStart(p0: Animation?) {
                mVideoControlsContainer!!.visibility = View.GONE
            }
        })
    }

    fun onUiVisibilityChanged(isShown: Boolean) {
        if (!isShown) mVideoControlsContainer!!.startAnimation(animCollapseControls)
        else mVideoControlsContainer!!.startAnimation(animExpandControls)
    }

    fun onConfigurationChanged(configuration: Configuration) {
        setupControlView()
        if (isPrepared) {
            enablePlayPauseButtonView(true)
            enableVideoProgress(true)
            enableSoundSwitcherButton(true)
            updateControlView()
        }
    }

    fun onBackPressed() {
        pauseVideoView()
    }

    fun setupControlView() {
        mVideoControlsContainer!!.removeAllViews()

        mVideoControlsLayout = mFragment.context.layoutInflater.inflate(
                R.layout.video_controls, mVideoControlsContainer, false) as FrameLayout
        mCurrentProgress = mVideoControlsLayout!!.find(R.id.current_progress)
        mVideoProgress = mVideoControlsLayout!!.find(R.id.video_progress)
        mOverallDuration = mVideoControlsLayout!!.find(R.id.overall_duration)
        mPlayPauseContainer = mVideoControlsLayout!!.find(R.id.play_pause_container)
        mPlayPauseImage = mVideoControlsLayout!!.find(R.id.play_pause)
        mSoundSwitcherContainer = mVideoControlsLayout!!.find(R.id.sound_switcher_container)
        mSoundSwitcherImage = mVideoControlsLayout!!.find(R.id.sound_switcher)

        mPlayPauseContainer!!.setOnClickListener {
            if (mVideoView!!.isPlaying) pauseVideoView(); else startVideoView()
        }
        if (isCompleted) {
            completeVideoView()
        } else {
            if (mVideoView!!.isPlaying) mPlayPauseImage!!.imageResource = R.drawable.ic_pause_white_24dp
            else mPlayPauseImage!!.imageResource = R.drawable.ic_play_arrow_white_24dp
        }

        mVideoProgress!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    if (isCompleted) {
                        isCompleted = false
                        mVideoView!!.restart()
                        mVideoView!!.seekTo(mDuration * progress / 100)
                        mPlayPauseImage!!.imageResource = R.drawable.ic_pause_white_24dp
                        mPlayPauseContainer!!.setOnClickListener {
                            if (mVideoView!!.isPlaying) pauseVideoView(); else startVideoView() }
                        mVideoView!!.setOnBufferUpdateListener { percent -> run {
                            mVideoProgress!!.secondaryProgress = percent }
                        }
                    } else {
                        if (!isPrepared) mVideoView!!.seekTo(mDuration * progress / 100)
                        else mVideoView!!.seekTo(mVideoView!!.duration * progress / 100)
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        mSoundSwitcherContainer!!.setOnClickListener {
            if (App.mSoundContentObserver!!.getStreamVolume() == 0) {
                App.mSoundContentObserver!!.setStreamVolume(previousVolume)
            } else {
                previousVolume = App.mSoundContentObserver!!.getStreamVolume()
                App.mSoundContentObserver!!.setStreamVolume(0)
            }
        }

        enablePlayPauseButtonView(false)
        enableVideoProgress(false)
        enableSoundSwitcherButton(false)

        mVideoControlsContainer!!.addView(mVideoControlsLayout)
        setupMarginsForControlView(mFragment.context.configuration)
        mVideoControlsContainer!!.bringToFront()
    }

    fun enablePlayPauseButtonView(enable: Boolean) {
        mPlayPauseContainer!!.isEnabled = enable
        mPlayPauseContainer!!.isClickable = enable
        mPlayPauseContainer!!.isFocusable = enable
    }

    fun enableVideoProgress(enable: Boolean) {
        mVideoProgress!!.isEnabled = enable
        mVideoProgress!!.isClickable = enable
        mVideoProgress!!.isFocusable = enable
    }

    fun enableSoundSwitcherButton(enable: Boolean) {
        mSoundSwitcherContainer!!.isEnabled = enable
        mSoundSwitcherContainer!!.isClickable = enable
        mSoundSwitcherContainer!!.isFocusable = enable
    }

    fun setupMarginsForControlView(configuration: Configuration) {
        if (DeviceUtils.sdkIsKitkatOrHigher() && DeviceUtils.deviceHasNavigationBar(mFragment.context)) {
            when (configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    mVideoControlsLayout!!.setPadding(0, 0, 0, UIUtils.convertDpToPixel(
                            mFragment.dimen(R.dimen.navigation_bar_size).toFloat()).toInt() / 2)
                }
                Configuration.ORIENTATION_LANDSCAPE -> {
                    mVideoControlsLayout!!.setPadding(0, 0, UIUtils.convertDpToPixel(
                            mFragment.dimen(R.dimen.navigation_bar_size).toFloat()).toInt() / 2, 0)
                }
            }
        }
    }

    fun pauseVideoView() {
        mVideoView!!.pause()
        mPlayPauseImage!!.imageResource = R.drawable.ic_play_arrow_white_24dp
    }

    fun startVideoView() {
        if (mFragment.isCurrentPosition()) {
            if (isCompleted) completeVideoView()
            else {
                mVideoView!!.start()
                mPlayPauseImage!!.imageResource = R.drawable.ic_pause_white_24dp
                enablePlayPauseButtonView(true)
                enableVideoProgress(true)
                enableSoundSwitcherButton(true)
                updateControlView()
            }
        }
    }

    fun completeVideoView() {
        Log.d(LOG_TAG, "video completed:")
        isCompleted = true
        mPlayPauseImage!!.imageResource = R.drawable.ic_refresh_white_24dp
        mPlayPauseContainer!!.setOnClickListener {
            isCompleted = false
            mVideoView!!.restart()
            mVideoProgress!!.progress = 0
            mPlayPauseImage!!.imageResource = R.drawable.ic_pause_white_24dp
            startVideoView()
            mPlayPauseContainer!!.setOnClickListener {
                if (mVideoView!!.isPlaying) pauseVideoView(); else startVideoView() }
            mVideoView!!.setOnBufferUpdateListener { percent -> run {
                mVideoProgress!!.secondaryProgress = percent }
            }
        }
    }

    fun onSoundChanged(volume: Int) {
        Log.d(LOG_TAG, "volume: $volume")
        Log.d(LOG_TAG, "maxVolume: ${App.mSoundContentObserver!!.getMaxVolume()}")

        if (volume == 0) {
            mSoundSwitcherImage!!.imageResource = R.drawable.ic_volume_off_white_24dp
        } else {
            mSoundSwitcherImage!!.imageResource = R.drawable.ic_volume_up_white_24dp
        }
    }

    fun updateControlView() {
        mOverallDuration!!.text = VideoUtils.getFormattedProgressString(mVideoView!!.duration)
        sHandler.post(updateRunnable)
    }

    val updateRunnable: Runnable = object : Runnable {
        override fun run() {
            mCurrentProgress!!.text =
                    VideoUtils.getFormattedProgressString(mVideoView!!.currentPosition)
            if (mVideoView!!.currentPosition != 0L && mVideoView!!.duration != 0L) {
                mVideoProgress!!.progress =
                        ((mVideoView!!.currentPosition * 100) / mVideoView!!.duration).toInt()
            }
            if ((mVideoView!!.currentPosition != 0L && mVideoView!!.duration != 0L) &&
                    (mVideoView!!.currentPosition == mVideoView!!.duration)) {
                completeVideoView(); sHandler.removeCallbacks(this)
            }
            sHandler.postDelayed(this,  100)
        }
    }

    override fun onClick(view: View?) {
        Log.d(LOG_TAG, "onClick:")
        mFragment.onClick()
    }

    fun onItemDestroy() {
        sHandler.removeCallbacks(updateRunnable)
        releaseVideoView()
    }

    fun releaseVideoView() {
        mVideoView!!.release()
    }

    override fun onPrepared() {
        isPrepared = true
        mProgressBar!!.visibility = View.GONE
        mDuration = mVideoView!!.duration
        startVideoView()
    }

    override fun onCompletion() {
        isPrepared = false
        completeVideoView()
    }
}
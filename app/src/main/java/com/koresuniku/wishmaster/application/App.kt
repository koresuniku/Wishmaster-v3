package com.koresuniku.wishmaster.application

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.koresuniku.wishmaster.http.HttpClient
import java.io.InputStream

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Glide.get(this).register(GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(HttpClient.client))
        BigImageViewer.initialize(GlideImageLoader.with(this))
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        App.Companion.soundVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        setupContentObserver()
    }


    private fun setupContentObserver() {
        App.Companion.mSoundContentObserver = SoundContentObserver(this.baseContext, Handler())
        contentResolver.registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true, mSoundContentObserver)
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(App.Companion.LOG_TAG, "onTerminate:")
        contentResolver.unregisterContentObserver(mSoundContentObserver)
    }

    companion object {
        internal val LOG_TAG = App::class.java.simpleName

        var soundVolume: Int = 0
        var mSoundContentObserver: SoundContentObserver? = null
    }

}
package com.koresuniku.wishmaster.system

import android.content.Context
import android.media.AudioManager
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.koresuniku.wishmaster.http.HttpClient
import java.io.InputStream

class App : android.app.Application() {
    // public static SettingsContentObserver mSettingsContentObserver;


    override fun onCreate() {
        super.onCreate()

        Glide.get(this).register(GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(HttpClient.client))
        BigImageViewer.initialize(GlideImageLoader.with(this))
        //BigImageViewer.initialize(FrescoImageLoader.with(this))
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        App.Companion.soundVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        setupContentObserver()
    }


    private fun setupContentObserver() {
        //mSettingsContentObserver = new SettingsContentObserver(this.getBaseContext(), new Handler());
        //getActivityOverridden().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);
    }

    override fun onTerminate() {
        super.onTerminate()
        android.util.Log.d(com.koresuniku.wishmaster.system.App.Companion.LOG_TAG, "onTerminate:")
        //getActivityOverridden().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    companion object {
        internal val LOG_TAG = App::class.java.simpleName

        var soundVolume: Int = 0
    }

    fun getContext(): Context {
        return applicationContext
    }

}
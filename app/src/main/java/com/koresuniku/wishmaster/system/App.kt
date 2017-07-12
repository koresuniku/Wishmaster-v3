package com.koresuniku.wishmaster.system

import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.koresuniku.wishmaster.http.HttpClient
import java.io.InputStream

class App : android.app.Application() {
    // public static SettingsContentObserver mSettingsContentObserver;


    override fun onCreate() {
        super.onCreate()

        android.util.Log.d("Application: ", "App")
        //BigImageViewer.initialize(GlideImageLoader.with(this));
        Glide.get(this).register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(HttpClient.client))
        val audio = getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
        com.koresuniku.wishmaster.system.App.Companion.soundVolume = audio.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
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
        internal val LOG_TAG = com.koresuniku.wishmaster.system.App::class.java.simpleName

        var soundVolume: Int = 0
    }

    fun getContext(): android.content.Context {
        return applicationContext
    }

}
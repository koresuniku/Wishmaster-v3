package com.koresuniku.wishmaster

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.util.Log

import com.koresuniku.wishmaster.http.HttpClient

import java.io.InputStream

class App : Application() {
    // public static SettingsContentObserver mSettingsContentObserver;


    override fun onCreate() {
        super.onCreate()

        Log.d("Application: ", "App")
        //BigImageViewer.initialize(GlideImageLoader.with(this));
        //Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpClient.INSTANCE.getClient()));
        val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        App.soundVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        setupContentObserver()

    }


    private fun setupContentObserver() {
        //mSettingsContentObserver = new SettingsContentObserver(this.getBaseContext(), new Handler());
        //getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(LOG_TAG, "onTerminate:")
        //getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    companion object {
        internal val LOG_TAG = App::class.java.simpleName

        var soundVolume: Int = 0
    }

    fun getContext(): Context {
        return applicationContext
    }

}
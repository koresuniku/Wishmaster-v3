package com.koresuniku.wishmaster.system

class App : android.app.Application() {
    // public static SettingsContentObserver mSettingsContentObserver;


    override fun onCreate() {
        super.onCreate()

        android.util.Log.d("Application: ", "App")
        //BigImageViewer.initialize(GlideImageLoader.with(this));
        //Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpClient.INSTANCE.getClient()));
        val audio = getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
        com.koresuniku.wishmaster.system.App.Companion.soundVolume = audio.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
        setupContentObserver()

    }


    private fun setupContentObserver() {
        //mSettingsContentObserver = new SettingsContentObserver(this.getBaseContext(), new Handler());
        //getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);
    }

    override fun onTerminate() {
        super.onTerminate()
        android.util.Log.d(com.koresuniku.wishmaster.system.App.Companion.LOG_TAG, "onTerminate:")
        //getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

    companion object {
        internal val LOG_TAG = com.koresuniku.wishmaster.system.App::class.java.simpleName

        var soundVolume: Int = 0
    }

    fun getContext(): android.content.Context {
        return applicationContext
    }

}
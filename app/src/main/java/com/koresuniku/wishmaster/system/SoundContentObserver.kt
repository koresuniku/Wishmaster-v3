package com.koresuniku.wishmaster.system

import android.content.Context
import android.media.AudioManager
import android.database.ContentObserver
import android.os.Handler
import android.util.Log
import com.koresuniku.wishmaster.ui.gallery.content.SoundVolumeChangeListener


class SoundContentObserver(internal var context: Context, handler: Handler) : ContentObserver(handler) {
    val LOG_TAG: String = SoundContentObserver::class.java.simpleName
    val audioService = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val listeners: HashSet<SoundVolumeChangeListener> = HashSet()

    init {
        App.soundVolume = audioService.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun deliverSelfNotifications(): Boolean {
        return super.deliverSelfNotifications()
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        Log.d(LOG_TAG, "onChange:")
        App.soundVolume = audioService.getStreamVolume(AudioManager.STREAM_MUSIC)
        listeners.forEach { it.onVolumeChanged(App.soundVolume) }
    }

    fun bindListener(soundVolumeChangeListener: SoundVolumeChangeListener) {
        listeners.add(soundVolumeChangeListener)
    }

    fun unbindListener(soundVolumeChangeListener: SoundVolumeChangeListener) {
        listeners.remove(soundVolumeChangeListener)
    }

    fun getMaxVolume(): Int {
        return audioService.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    fun setStreamVolume(volume: Int) {
        audioService.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    fun getStreamVolume(): Int {
        return audioService.getStreamVolume(AudioManager.STREAM_MUSIC)
    }
}
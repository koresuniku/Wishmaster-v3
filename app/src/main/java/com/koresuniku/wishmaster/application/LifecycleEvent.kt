package com.koresuniku.wishmaster.application

import android.content.res.Configuration

class LifecycleEvent(val anEvent: Int) {
    lateinit var configuration: Configuration
    companion object {
        val onStart: Int = 0
        val onStop: Int = 1
        val onConfigurationChanged: Int = 2
    }
}
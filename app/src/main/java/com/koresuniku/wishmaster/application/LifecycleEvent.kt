package com.koresuniku.wishmaster.application

import android.content.res.Configuration

class LifecycleEvent(val anEvent: Int) {
    lateinit var configuration: Configuration
    companion object {
        val ON_START: Int = 0
        val ON_STOP: Int = 1
        val ON_CONFIGURATION_CHANGED: Int = 2
    }
}
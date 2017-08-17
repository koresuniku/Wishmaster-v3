package com.koresuniku.wishmaster.application

class LifecycleEvent(val anEvent: Int) {
    companion object {
        val onStart: Int = 0
        val onStop: Int = 1
    }
}
package com.koresuniku.wishmaster.ui.gallery.content

import java.util.concurrent.TimeUnit

object VideoUtils {

    private var minutes: Long = 0
    private var minutesString: String? = null
    private var seconds: Long = 0
    private var secondsString: String? = null

    fun getFormattedProgressString(position: Long): String {
        minutes = TimeUnit.MILLISECONDS.toMinutes(position)
        seconds = TimeUnit.MILLISECONDS.toSeconds(position)
        if (minutes < 10) minutesString = "0" + minutes
        secondsString = (seconds - minutes * 60).toString()
        if (Integer.parseInt(secondsString) < 10) secondsString = "0" + secondsString
        return minutesString + ":" + secondsString
    }
}
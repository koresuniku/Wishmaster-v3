package com.koresuniku.wishmaster.ui.view

import android.app.Activity
import android.content.Context
import android.view.View

interface ProgressView {
    fun getActivityOverridden(): Activity

    fun getProgressContainer(): View
}
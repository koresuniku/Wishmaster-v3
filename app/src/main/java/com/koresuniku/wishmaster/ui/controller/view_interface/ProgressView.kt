package com.koresuniku.wishmaster.ui.controller.view_interface

import android.app.Activity
import android.view.View

interface ProgressView {
    fun getActivityOverridden(): Activity

    fun getProgressContainer(): View
}
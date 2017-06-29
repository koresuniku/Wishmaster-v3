package com.koresuniku.wishmaster.ui.controller

import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import com.koresuniku.wishmaster.ui.view.AppBarLayoutView

class AppBarLayoutUnit(val mView: AppBarLayoutView) {
    val LOG_TAG: String = AppBarLayoutUnit::class.java.simpleName

    val mAppBarLayout: AppBarLayout = mView.getAppBarLayout()

    init {
        onCreate()
    }

    fun onCreate() {
        //ViewCompat.setElevation(mAppBarLayout, 2f)
        mAppBarLayout.targetElevation = 2f
    }
}
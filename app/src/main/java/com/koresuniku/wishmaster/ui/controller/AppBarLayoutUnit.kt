package com.koresuniku.wishmaster.ui.controller

import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import com.koresuniku.wishmaster.ui.view.AppBarLayoutView

class AppBarLayoutUnit(val mView: AppBarLayoutView) {
    val LOG_TAG: String = AppBarLayoutUnit::class.java.simpleName

    val mAppBarLayout: AppBarLayout = mView.getAppBarLayout()
    var appBarVerticalOffset: Int = 0
    var appBarLayoutExpandedValue: Int = mAppBarLayout.totalScrollRange

    init {
        onCreate()
    }

    fun onCreate() {
        mAppBarLayout.addOnOffsetChangedListener(
                { appBarLayout, verticalOffset -> appBarVerticalOffset = verticalOffset })
    }
}
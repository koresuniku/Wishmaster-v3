package com.koresuniku.wishmaster.ui.view

import android.app.Activity
import android.support.design.widget.AppBarLayout

interface AppBarLayoutView {
    fun getActivity(): Activity

    fun getAppBarLayout(): AppBarLayout
}
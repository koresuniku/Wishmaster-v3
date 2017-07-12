package com.koresuniku.wishmaster.ui.view

import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout

interface ActionBarView {
    fun getToolbarContainer(): FrameLayout

    fun getAppCompatActivity(): AppCompatActivity

    fun setupActionBarTitle()
}
package com.koresuniku.wishmaster.ui.view

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout

interface ActionBarView {
    fun getToolbarContainer(): FrameLayout

    fun getAppCompatActivity(): AppCompatActivity

    fun setupActionBarTitle()

    fun addTabs(): Boolean

    fun getViewPager(): ViewPager


}
package com.koresuniku.wishmaster.ui.controller.view_interface

import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout

interface ActionBarView {
    fun getToolbarContainer(): FrameLayout

    fun getAppCompatActivity(): AppCompatActivity

    fun setupActionBarTitle()

    fun onBackPressedOverridden(): Boolean
}
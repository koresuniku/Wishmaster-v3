package com.koresuniku.wishmaster.ui.controller.view_interface

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.widget.FrameLayout
import com.koresuniku.wishmaster.system.PreferenceUtils
import com.koresuniku.wishmaster.ui.dashboard.BoardListFragment
import com.koresuniku.wishmaster.ui.dashboard.FavouritesFragment
import com.koresuniku.wishmaster.ui.dashboard.HistoryFragment

interface ActionBarWithTabsView {
    fun getToolbarContainer(): FrameLayout

    fun getAppCompatActivity(): AppCompatActivity

    fun setupActionBarTitle()

    fun addTabs(): Boolean

    fun getViewPager(): ViewPager

    fun getBoardListFragment(): BoardListFragment

    fun getFavouritesFragment(): FavouritesFragment

    fun getHistoryFragment(): HistoryFragment

    fun getPreferencesManager(): PreferenceUtils

    fun getMenu(): Menu?

    fun getContentContainer(): FrameLayout

}
package com.koresuniku.wishmaster.system

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {

    val DASHBOARD_TAB_POSITION_KEY: String = "dashboard_tab_position_key"
    val DASHBOARD_TAB_POSITION_FAVOURITES: Int = 0
    val DASHBOARD_TAB_POSITION_BOARD_LIST_DEFAULT: Int = 1
    val DASHBOARD_TAB_POSITION_HISTORY: Int = 2

    fun getSharedPreferences(context: Activity): SharedPreferences {
        return context.getPreferences(Context.MODE_PRIVATE)
    }

    fun addnew() {}
}

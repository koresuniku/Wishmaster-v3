package com.koresuniku.wishmaster.system

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.regex.Pattern

object PreferencesManager {
    val LOG_TAG: String = PreferencesManager::class.java.simpleName

    val DASHBOARD_TAB_POSITION_KEY: String = "dashboard_tab_position_key"
    val DASHBOARD_TAB_POSITION_FAVOURITES: Int = 0
    val DASHBOARD_TAB_POSITION_BOARD_LIST_DEFAULT: Int = 1
    val DASHBOARD_TAB_POSITION_HISTORY: Int = 2

    val FAVOUTITE_BOARDS_QUEUE_KEY: String = "favourite_boards_queue_key"
    val FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT: String = ""


    fun getSharedPreferences(context: Activity): SharedPreferences {
        return context.getPreferences(Context.MODE_PRIVATE)
    }

    fun addNewFavouriteBoard(activity: Activity, boardId: String) {
        var queue = getSharedPreferences(activity).getString(
                FAVOUTITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)
        queue += " "
        queue += boardId

        Log.d(LOG_TAG, "queue: " + queue)

        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOUTITE_BOARDS_QUEUE_KEY, queue)
        editor.commit()
    }

    fun deleteFavouriteBoard(activity: Activity, boardId: String) {
        val queue = getSharedPreferences(activity).getString(
                FAVOUTITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)

        Log.d(LOG_TAG, "queue: " + queue)
        Log.d(LOG_TAG, "removing board: " + boardId)
        var result: String = queue.replace(" " + boardId, "")
        Log.d(LOG_TAG, "result: " + result)

        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOUTITE_BOARDS_QUEUE_KEY, result)
        editor.commit()
    }

    fun getFavouriteBoardsQueue(activity: Activity): String {
        val queue = getSharedPreferences(activity).getString(
                FAVOUTITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)
        return queue
    }

    fun writeInFavouriteBoardsQueue(activity: Activity, queue: String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOUTITE_BOARDS_QUEUE_KEY, queue)
        editor.commit()
    }
}

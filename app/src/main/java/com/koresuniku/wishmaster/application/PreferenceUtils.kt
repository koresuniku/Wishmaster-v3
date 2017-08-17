package com.koresuniku.wishmaster.application

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.koresuniku.wishmaster.R

object PreferenceUtils {
    val LOG_TAG: String = PreferenceUtils::class.java.simpleName

    val FAVOURITE_BOARDS_QUEUE_KEY: String = "favourite_boards_queue_key"
    val FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT: String = ""

    fun getSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun addNewFavouriteBoard(activity: Activity, boardId: String) {
        var queue = getSharedPreferences(activity).getString(
                FAVOURITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)
        queue += " "
        queue += boardId

        Log.d(LOG_TAG, "queue: " + queue)

        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOURITE_BOARDS_QUEUE_KEY, queue)
        editor.commit()
    }

    fun deleteFavouriteBoard(activity: Activity, boardId: String) {
        val queue = getSharedPreferences(activity).getString(
                FAVOURITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)

        Log.d(LOG_TAG, "queue: " + queue)
        Log.d(LOG_TAG, "removing board: " + boardId)
        var result: String = queue.replace(" " + boardId, "")
        Log.d(LOG_TAG, "result: " + result)

        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOURITE_BOARDS_QUEUE_KEY, result)
        editor.commit()
    }

    fun getFavouriteBoardsQueue(activity: Activity): String {
        val queue = getSharedPreferences(activity).getString(
                FAVOURITE_BOARDS_QUEUE_KEY,
                FAVOURITE_BOARDS_QUEUE_EMPTY_DEFAULT)
        return queue
    }

    fun writeInFavouriteBoardsQueue(activity: Activity, queue: String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(activity).edit()
        editor.putString(FAVOURITE_BOARDS_QUEUE_KEY, queue)
        editor.commit()
    }

    fun getPreferredMaximumImageHeightInDp(activity: Activity): Float {
        val value = getSharedPreferences(activity).getString(
                activity.getString(R.string.pref_image_height_max_key),
                activity.getString(R.string.pref_image_height_max_default))
        return value.toFloat()
    }

    fun getPreferredMinimumImageHeightInDp(activity: Activity): Float {
        val value = getSharedPreferences(activity).getString(
                activity.getString(R.string.pref_image_height_min_key),
                activity.getString(R.string.pref_image_height_min_default))
        return value.toFloat()
    }


}

package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.database.DatabaseContract
import com.koresuniku.wishmaster.system.PreferencesManager

object DialogManager {
    val DIALOG_DASHBOARD_ID: Int = 0
    val DIALOG_REMOVE_FROM_FAVOURITES_KEY: String = "dialog_remove_from_favourites_key"
    val DIALOG_BOARD_ID_KEY: String = "dialog_board_id_key"

    interface DashBoardActivityCallback {
        fun removeFromFavourites(boardId: String)
        fun addNewFavouriteBoard(boardId: String)
        fun getActivity(): Activity
    }

    fun createDashBoardDialog(view: DashBoardActivityCallback, id: Int, args: Bundle?): Dialog {
        val builder = AlertDialog.Builder(view.getActivity())

        builder.setItems(if (args!!.getBoolean(DIALOG_REMOVE_FROM_FAVOURITES_KEY))
            R.array.on_board_clicked_choice_remove else
            R.array.on_board_clicked_choice_add, {
            dialog, which -> run {
            if (which == 2) {
                if (args.getBoolean(DIALOG_REMOVE_FROM_FAVOURITES_KEY)) {
                    view.removeFromFavourites(boardId = args.getString(DIALOG_BOARD_ID_KEY))
                } else {
                    view.addNewFavouriteBoard(boardId = args.getString(DIALOG_BOARD_ID_KEY))
                }
            }
        }

        })
        builder.setCancelable(true)

        return builder.create()
    }

}
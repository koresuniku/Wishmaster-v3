package com.koresuniku.wishmaster.ui.controller

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.koresuniku.wishmaster.R

object DialogManager {
    val DIALOG_DASHBOARD_ID = 0

    val DIALOG_OPEN_BOARD_CODE: Int = 0
    val DIALOG_COPY_LINK_CODE: Int = 1
    val DIALOG_ADD_REMOVE_BOARD_CODE: Int = 2
    val DIALOG_REMOVE_FROM_FAVOURITES_KEY: String = "dialog_remove_from_favourites_key"
    val DIALOG_BOARD_ID_KEY: String = "dialog_board_id_key"
    val DIALOG_BOARD_NAME_KEY: String = "dialog_board_name_key"

    interface DashBoardActivityCallback {
        fun removeFromFavourites(boardId: String)
        fun addNewFavouriteBoard(boardId: String)
        fun openBoard(boardId: String, boardName: String)
        fun getActivity(): Activity
    }

    fun createDashBoardDialog(view: DashBoardActivityCallback, args: Bundle?): Dialog {
        val builder = AlertDialog.Builder(view.getActivity())

        builder.setItems(if (args!!.getBoolean(DIALOG_REMOVE_FROM_FAVOURITES_KEY))
            R.array.on_board_clicked_choice_remove else
            R.array.on_board_clicked_choice_add, {
            dialog, which -> run {
            when (which) {
                DIALOG_OPEN_BOARD_CODE -> {
                    view.openBoard(
                            boardId = args.getString(DIALOG_BOARD_ID_KEY),
                            boardName = args.getString(DIALOG_BOARD_NAME_KEY))
                }
                DIALOG_COPY_LINK_CODE -> {

                }
                DIALOG_ADD_REMOVE_BOARD_CODE -> {
                    if (args.getBoolean(DIALOG_REMOVE_FROM_FAVOURITES_KEY)) {
                        view.removeFromFavourites(boardId = args.getString(DIALOG_BOARD_ID_KEY))
                    } else {
                        view.addNewFavouriteBoard(boardId = args.getString(DIALOG_BOARD_ID_KEY))
                    }
                }
            }
        }

        })
        builder.setCancelable(true)

        return builder.create()
    }

}
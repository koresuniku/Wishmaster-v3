package com.koresuniku.wishmaster.ui.controller

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.controller.view_interface.IActivityView


//import netscape.javascript.JSObject.getWindow
//import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil.getAttributes



object DialogManager {
    val DIALOG_DASHBOARD_ID = 0

    val DIALOG_OPEN_BOARD_CODE: Int = 0
    val DIALOG_COPY_LINK_CODE: Int = 1
    val DIALOG_ADD_REMOVE_BOARD_CODE: Int = 2
    val DIALOG_REMOVE_FROM_FAVOURITES_KEY: String = "dialog_remove_from_favourites_key"
    val DIALOG_BOARD_ID_KEY: String = "dialog_board_id_key"
    val DIALOG_BOARD_NAME_KEY: String = "dialog_board_name_key"

    val DIALOG_THREAD_ITEM_POSITION_KEY = "thread_item_position_key"
    val DIALOG_THREAD_ITEM_OPEN_POST = 0
    val DIALOG_THREAD_ITEM_COPY_LINK_CODE: Int = 1


    interface DashBoardActivityCallback : IActivityView {
        fun removeFromFavourites(boardId: String)
        fun addNewFavouriteBoard(boardId: String)
        fun openBoard(boardId: String, boardName: String)
    }

    interface PostCallBack : IActivityView {
        fun openFullPost(position: Int)
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

    fun createPostDialog(view: PostCallBack, args: Bundle?): Dialog {
        val builder = AlertDialog.Builder(view.getActivity())
        builder.setItems(R.array.on_thread_item_clicked, {
            dialog, which -> run {
            when (which) {
                DIALOG_THREAD_ITEM_OPEN_POST -> {
                    view.openFullPost(args!!.getInt(DIALOG_THREAD_ITEM_POSITION_KEY))
                }
                DIALOG_THREAD_ITEM_COPY_LINK_CODE -> {
                    
                }
            }

        }
        })

        builder.setCancelable(true)
        return builder.create()
    }

//    fun createFullPostDialog(activity: Activity, view: View): Dialog {
//        val builder = AlertDialog.Builder(activity)
//        builder.setCancelable(true)
//        builder.setView(view)
//
//
//        val dialogContentView = builder.create()
//
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialogContentView.window.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.gravity = Gravity.CENTER
//
//        dialogContentView.window.attributes = lp
//
//        //return builder.create()
//        return dialogContentView
//    }

}
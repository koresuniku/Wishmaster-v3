package com.koresuniku.wishmaster.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.LifecycleEvent
import com.koresuniku.wishmaster.ui.controller.view_interface.IActivityView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLifecycleEvent(event: LifecycleEvent) {
        when (event.anEvent) {
            LifecycleEvent.onStart ->
                if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
            LifecycleEvent.onStop ->
                if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
        }
    }

    interface DashBoardActivityCallback : IActivityView {
        fun removeFromFavourites(boardId: String)
        fun addNewFavouriteBoard(boardId: String)
        fun openBoard(boardId: String, boardName: String)
    }

    interface GalleryVisibilityListener {
        fun onGalleryShown()
        fun onGalleryHidden()
    }

    interface ThreadPostCallback : IActivityView {
        fun openFullPost(position: Int)
    }

    val DIALOG_POST_ITEM_POST_NUMBER_KEY: String = "post_item_post_number_key"
    val DIALOG_POST_ITEM_OPEN_ANSWER_ACTIVITY: Int = 0
    val DIALOG_POST_ITEM_COPY_LINK: Int = 1
    interface PostDialogView : IActivityView {
        fun openPostingActivity(postNumber: String)
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

    fun createThreadPostDialog(view: ThreadPostCallback, args: Bundle?): Dialog {
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

    fun createAndShowPostDialog(dialogView: PostDialogView, args: Bundle) {
        val builder = AlertDialog.Builder(dialogView.getActivity())
        builder.setItems(R.array.on_post_item_clicked, {
            dialog, which -> run {
            when (which) {
                DIALOG_POST_ITEM_OPEN_ANSWER_ACTIVITY -> {
                    dialogView.openPostingActivity(args.getString(DIALOG_POST_ITEM_POST_NUMBER_KEY))
                }
                DIALOG_THREAD_ITEM_COPY_LINK_CODE -> {

                }
            }
        }
        })

        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
    }

}
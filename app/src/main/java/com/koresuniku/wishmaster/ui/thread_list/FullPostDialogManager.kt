package com.koresuniku.wishmaster.ui.thread_list

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import com.koresuniku.wishmaster.ui.controller.DialogManager
import com.koresuniku.wishmaster.ui.single_thread.answers.ScrollsHolder

class FullPostDialogManager(var mView: ThreadListActivity) {
    val LOG_TAG: String = FullPostDialogManager::class.java.simpleName

    //var currentScrollFullPostDialog: ScrollsHolder? = null
    var mDialog: Dialog? = null
    var mScrollView: ScrollView? = null
    var isDialogShown: Boolean = false

    fun dismissFullPostDialog() {
        if (isDialogShown) {
            Log.d(LOG_TAG, "mScrollView.scrollY: ${mScrollView!!.scrollY}")
            //currentScrollFullPostDialog = ScrollsHolder(mScrollView!!.scrollY)
            mDialog!!.dismiss()
        }
    }

    fun showFullPostDialog() {
        if (isDialogShown) {
            mDialog!!.show()
        }
    }


    fun openFullPost(position: Int) {
        isDialogShown = true

        val args: Bundle = Bundle()
        args.putInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY, position)
        mView.removeDialog(mView.DIALOG_FULL_POST_ID)

        mDialog = Dialog(mView)
        mScrollView = ScrollView(mView)
        val itemView: View = mView.mThreadListListViewUnit!!.mListViewAdapter!!.getViewForDialog(
                args.getInt(DialogManager.DIALOG_THREAD_ITEM_POSITION_KEY), null, mScrollView!!)
        mScrollView!!.addView(itemView)
        mDialog!!.setContentView(mScrollView)
        mDialog!!.window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        mDialog!!.setOnKeyListener(OnKeyListener())
        mDialog!!.setCancelable(true)

        mDialog!!.show()
    }

    inner class OnKeyListener : DialogInterface.OnKeyListener {
        override fun onKey(p0: DialogInterface?, p1: Int, p2: KeyEvent?): Boolean {
            if (p1 == KeyEvent.KEYCODE_BACK) {
                if (isDialogShown) isDialogShown = false
                else mDialog!!.dismiss()
                return true
            }

            return false
        }
    }

}
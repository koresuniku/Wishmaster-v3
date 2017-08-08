package com.koresuniku.wishmaster.ui.text

import android.text.Spannable
import android.view.MotionEvent
import android.widget.TextView

/**
 * Created by koresuniku on 08.08.17.
 */
interface ICommentLinkMovementMethodPresenter {
    fun initFixSpoilerSpans(spannable: Spannable)

    fun onActionUp(widget: TextView, buffer: Spannable, event: MotionEvent)

    fun onActionDown(widget: TextView, buffer: Spannable, event: MotionEvent)

    fun onActionCancel(widget: TextView, buffer: Spannable, event: MotionEvent)

}
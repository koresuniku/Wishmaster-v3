package com.koresuniku.wishmaster.ui.text.comment_link_movement_method

import android.text.Spannable
import android.view.MotionEvent
import android.widget.TextView

interface ICommentLinkMovementMethodPresenter {
    fun initFixSpoilerSpans(spannable: Spannable)

    fun onActionUp(widget: TextView, buffer: Spannable, event: MotionEvent)

    fun onActionDown(widget: TextView, buffer: Spannable, event: MotionEvent)

    fun onActionCancel(widget: TextView, buffer: Spannable, event: MotionEvent)

}
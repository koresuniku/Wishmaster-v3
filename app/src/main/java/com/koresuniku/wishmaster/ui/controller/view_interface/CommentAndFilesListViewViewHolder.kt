package com.koresuniku.wishmaster.ui.controller.view_interface

import android.app.Activity
import android.widget.TextView
import com.koresuniku.wishmaster.ui.controller.FilesListViewViewHolder
import com.koresuniku.wishmaster.ui.single_thread.SingleThreadListViewAdapter
import com.koresuniku.wishmaster.ui.text.comment_leading_margin_span.CommentLeadingMarginSpan

abstract class CommentAndFilesListViewViewHolder : FilesListViewViewHolder() {
    var mCommentTextView: TextView? = null

    open var imageContainerHeight: Int? = null

    abstract fun getActivity(): Activity


}
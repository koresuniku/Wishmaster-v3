package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.widget.TextView
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder

interface ICommentLeadingMarginSpanPresenter {
    fun computeLeadingMarginLineCount(): Int

    fun computeLeadingMarginWidth(): Int

    fun getEndOfSpan(widget: TextView): Int

}
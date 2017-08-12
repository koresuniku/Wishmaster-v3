package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.LoginFilter
import android.text.style.LeadingMarginSpan
import android.util.Log
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.dimen

class CommentLeadingMarginSpan(val holder: CommentAndFilesListViewViewHolder) :
        LeadingMarginSpan.LeadingMarginSpan2, ICommentLeadingMarginSpan {
    val LOG_TAG: String = CommentLeadingMarginSpan::class.java.simpleName

    var presenter: CommentLeadingMarginSpanPresenter? = null
    var marginLineCount: Int? = null
    var marginWidth: Int? = null

    companion object {
        fun computeLeadingMarginWidth(holder: CommentAndFilesListViewViewHolder): Int {
            return holder.imageAndSummaryContainer!!.width +
                    holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
        }

        fun computeLinesToBeSpanned(holder: CommentAndFilesListViewViewHolder): Int {
            val imageContainerHeightInDp = computeImageContainerHeightInDp(holder)
            val commentLineHeightInDp = computeCommentLineHeightInDp(holder)
            return Math.ceil((imageContainerHeightInDp / commentLineHeightInDp).toDouble()).toInt()
        }

        fun computeImageContainerHeightInDp(holder: CommentAndFilesListViewViewHolder): Float {
            val containerView = holder.imageAndSummaryContainer
            val imageViewHeight = ListViewAdapterUtils
                    .computeImageHeightInPx(holder.getActivity(), holder.files!![0], false)
            val summaryLineHeight = holder.summary!!.lineHeight
            val summaryHeight = if (Formats.VIDEO_FORMATS.contains(
                    TextUtils.getSubstringAfterDot(holder.files!![0].getPath())))
                summaryLineHeight * 3 else summaryLineHeight * 2
            val additionalMarginBottom =
                    containerView!!.context.dimen(R.dimen.post_item_text_margin_flow).toFloat()
            return UIUtils.convertPixelsToDp(imageViewHeight + summaryHeight + additionalMarginBottom)
        }

        fun computeCommentLineHeightInDp(holder: CommentAndFilesListViewViewHolder): Float {
            val commentTextView = holder.mCommentTextView
            return UIUtils.convertPixelsToDp(commentTextView!!.lineHeight.toFloat())
        }

    }

    init {
//        presenter = CommentLeadingMarginSpanPresenter(this)
//        presenter!!.commentLineCount = lineCount
//        marginLineCount = presenter!!.computeLeadingMarginLineCount()
//        marginWidth = presenter!!.computeLeadingMarginWidthInPx()
        marginLineCount = computeLinesToBeSpanned(holder)
        marginWidth = computeLeadingMarginWidth(holder)
    }

    override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int,
                                   bottom: Int, text: CharSequence?, start: Int, end: Int,
                                   first: Boolean, layout: Layout?) {
        Log.d(LOG_TAG, "direction: $dir")

    }


    override fun getLeadingMarginLineCount(): Int {
        return marginLineCount!!
    }

    override fun getLeadingMargin(first: Boolean): Int {
        if (first) return marginWidth!! else return 0
    }


    override fun getViewHolder(): CommentAndFilesListViewViewHolder {
        return holder
    }

//    fun getEndOfSpan(widget: TextView): Int {
//        return presenter!!.getEndOfSpan(widget)
//    }
//
//    fun setLineCount(lineCount: Int) {
//        presenter!!.commentLineCount = lineCount
//    }
}
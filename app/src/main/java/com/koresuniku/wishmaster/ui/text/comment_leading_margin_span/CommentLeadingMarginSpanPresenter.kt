package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.util.Log
import android.widget.TextView
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.controller.ImageManager
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.dimen

class CommentLeadingMarginSpanPresenter(leadingMarginSpan: ICommentLeadingMarginSpan) :
        ICommentLeadingMarginSpanPresenter {
    val LOG_TAG: String = CommentLeadingMarginSpanPresenter::class.java.simpleName

    val holder = leadingMarginSpan.getViewHolder()
    var linesOfCommentTextViewContainsImageContainer = 0
    var commentLineCount: Int = 0

    override fun computeLeadingMarginLineCount(): Int {
        if (holder.viewType == ListViewAdapterUtils.ITEM_SINGLE_IMAGE) {
            val imageContainerHeightInDp = computeImageContainerHeightInDp()
            val commentLineHeightInDp = computeCommentLineHeightInDp(holder)
            val commentLineCount = commentLineCount
            val approximateCommentHeightInDp = commentLineHeightInDp * commentLineCount

            Log.d(LOG_TAG, "comment line count: $commentLineCount")

            if (imageContainerHeightInDp >= approximateCommentHeightInDp) return commentLineCount
            else {
                linesOfCommentTextViewContainsImageContainer = Math.ceil(
                        (imageContainerHeightInDp / commentLineHeightInDp).toDouble()).toInt()
                return linesOfCommentTextViewContainsImageContainer
            }

        }
        return 0
    }

    fun computeImageContainerHeightInDp(): Float {
        val containerView = holder.imageAndSummaryContainer
        Log.d(LOG_TAG, "containerView.height: ${containerView!!.height}")
        val imageViewHeight = ListViewAdapterUtils
                .computeImageHeightInPx(holder.getActivity(), holder.files!![0], false)
        val summaryLineHeight = holder.summary!!.lineHeight
        val summaryHeight = if (Formats.VIDEO_FORMATS.contains(
                TextUtils.getSubstringAfterDot(holder.files!![0].getPath())))
            summaryLineHeight * 3 else summaryLineHeight * 2
        val additionalMarginBottom =
                containerView.context.dimen(R.dimen.post_item_text_margin_flow).toFloat()

        Log.d(LOG_TAG, "computedHeight.height: ${imageViewHeight + summaryHeight + additionalMarginBottom}")
        return UIUtils.convertPixelsToDp(imageViewHeight + summaryHeight + additionalMarginBottom)
    }

    fun computeCommentLineHeightInDp(holder: CommentAndFilesListViewViewHolder): Float {
        val commentTextView = holder.mCommentTextView
        return UIUtils.convertPixelsToDp(commentTextView!!.lineHeight.toFloat())
    }

    override fun getEndOfSpan(widget: TextView): Int {
        //widget.requestLayout()
        if (linesOfCommentTextViewContainsImageContainer < 1) return 0
        var lineEnd = widget.layout.getLineStart(linesOfCommentTextViewContainsImageContainer - 1)
        if (lineEnd > widget.text.length) lineEnd = widget.text.length
        Log.d(LOG_TAG, "lineEnd: $lineEnd")
        return lineEnd
    }

    override fun computeLeadingMarginWidth(): Int {
        if (holder.viewType == ListViewAdapterUtils.ITEM_SINGLE_IMAGE) {
            return holder.imageAndSummaryContainer!!.width +
                    holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
        } else return 0
    }
}
package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.view.View
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.dimen

class CommentLeadingMarginSpan2(val every: Int, val lineCount: Int) :
        LeadingMarginSpan.LeadingMarginSpan2 {
    val LOG_TAG: String = CommentLeadingMarginSpan2::class.java.simpleName


    init {

    }

    companion object {
        val LOG_TAG: String = CommentLeadingMarginSpan2::class.java.simpleName

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

        fun computeLeadingMarginWidthInPx(holder: CommentAndFilesListViewViewHolder): Int {
            holder.imageAndSummaryContainer!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return holder.imageAndSummaryContainer!!.measuredWidth +
                    holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
        }

        fun computeCommentTextViewWidthInPx(holder: CommentAndFilesListViewViewHolder): Int {
            val commentTextView = holder.mCommentTextView!!
            commentTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val displayWidth = holder.getActivity().windowManager.defaultDisplay.width
            val marginWidth = computeLeadingMarginWidthInPx(holder)
            val paddingWidth = holder.imageAndSummaryContainer!!.context.dimen(R.dimen.post_item_side_padding)
            //Log.d(LOG_TAG, "display width: $displayWidth")
            //Log.d(LOG_TAG, "margin width: $marginWidth")
            //Log.d(LOG_TAG, "padding width: $paddingWidth")
            return displayWidth - (paddingWidth * 2) - marginWidth
        }
    }

    override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int,
                                   bottom: Int, text: CharSequence?, start: Int, end: Int,
                                   first: Boolean, layout: Layout?) {
//        Log.d(LOG_TAG, "normal layout lines: ${layout!!.lineCount}")
//        val l = StaticLayout(text, TextPaint(), 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false)
//        Log.d(LOG_TAG, "small-width layout lines: ${l.lineCount}")
////        super.drawLeadingMargin(c, p, x, dir, top, baseline, bottom, text, start, end, first, layout)
//        super.drawLeadingMargin(c, p, x, dir, top, baseline, bottom, text, start, end, first, l)
    }

    override fun getLeadingMargin(first: Boolean): Int {
        //if (first) return every else return 0
        return every
    }


    override fun getLeadingMarginLineCount(): Int {
        return lineCount
    }
}
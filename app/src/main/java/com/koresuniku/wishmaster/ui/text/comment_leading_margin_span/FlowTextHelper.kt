package com.koresuniku.wishmaster.ui.text.comment_leading_margin_span

import android.graphics.Canvas
import android.graphics.Paint
import android.widget.RelativeLayout
import android.text.SpannableString
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import android.view.View
import android.widget.TextView
import android.text.Layout
import android.os.Build
import android.text.Spannable
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.ui.UIUtils
import com.koresuniku.wishmaster.ui.controller.ListViewAdapterUtils
import com.koresuniku.wishmaster.ui.controller.view_interface.CommentAndFilesListViewViewHolder
import com.koresuniku.wishmaster.ui.text.TextUtils
import com.koresuniku.wishmaster.util.Formats
import org.jetbrains.anko.dimen

object FlowTextHelper {

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

    fun flowTheText(spannable: Spannable, holder: CommentAndFilesListViewViewHolder) {

    }

    fun makeTextFlowable(spannable: Spannable, holder: CommentAndFilesListViewViewHolder) {
        val textView = holder.mCommentTextView!!

    }


    class MyLeadingMarginSpan2(private val lines: Int, private val margin: Int) : LeadingMarginSpan2 {

        override fun getLeadingMargin(first: Boolean): Int {
            return if (first) this.margin else 0
        }

        override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int,
                                       baseline: Int, bottom: Int, text: CharSequence,
                                       start: Int, end: Int, first: Boolean, layout: Layout) {}

        override fun getLeadingMarginLineCount(): Int {
            return this.lines
        }
    }
}